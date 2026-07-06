package com.powertrack.service;

import com.powertrack.dto.mapper.MeterReadingMapper;
import com.powertrack.dto.request.meterreading.MeterReadingRequestDTO;
import com.powertrack.dto.response.meterreading.MeterReadingResponseDTO;
import com.powertrack.entity.Meter;
import com.powertrack.entity.MeterReading;
import com.powertrack.entity.User;
import com.powertrack.enums.ReadingStatus;
import com.powertrack.enums.ReadingType;
import com.powertrack.exception.ResourceNotFoundException;
import com.powertrack.repository.MeterReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeterReadingService {

    private final MeterReadingRepository meterReadingRepository;
    private final MeterService meterService;
    private final UserService userService;

    @Transactional
    public MeterReadingResponseDTO submitMeterReading(MeterReadingRequestDTO request, Long fieldStaffId) {
        Meter meter = meterService.getMeterEntityById(request.getMeterId());
        User fieldStaff = userService.getUserEntityById(fieldStaffId);

        // Check if reading already exists for this month/year
        if (meterReadingRepository.existsByMeterIdAndReadingMonthAndReadingYear(
                request.getMeterId(), request.getReadingMonth(), request.getReadingYear())) {
            throw new IllegalStateException("Reading already exists for this meter in " +
                    request.getReadingMonth() + " " + request.getReadingYear());
        }

        // Get previous reading
        Integer previousReading = meter.getLastReading();

        // Validate current reading
        if (request.getCurrentReading() < previousReading) {
            throw new IllegalArgumentException("Current reading cannot be less than previous reading");
        }

        // Calculate units consumed
        Integer unitsConsumed = request.getCurrentReading() - previousReading;

        // Create meter reading
        MeterReading meterReading = new MeterReading();
        meterReading.setMeter(meter);
        meterReading.setFieldStaff(fieldStaff);
        meterReading.setPreviousReading(previousReading);
        meterReading.setCurrentReading(request.getCurrentReading());
        meterReading.setUnitsConsumed(unitsConsumed);
        meterReading.setReadingDate(request.getReadingDate());
        meterReading.setReadingMonth(request.getReadingMonth());
        meterReading.setReadingYear(request.getReadingYear());
        meterReading.setReadingType(ReadingType.valueOf(request.getReadingType()));
        meterReading.setPhotoUrl(request.getPhotoUrl());
        meterReading.setNotes(request.getNotes());
        meterReading.setStatus(ReadingStatus.SUBMITTED);

        MeterReading savedReading = meterReadingRepository.save(meterReading);

        // Update meter's last reading
        meterService.updateLastReading(meter.getId(), request.getCurrentReading());

        return MeterReadingMapper.toDTO(savedReading);
    }

    @Transactional(readOnly = true)
    public List<MeterReadingResponseDTO> getAllMeterReadings() {
        return meterReadingRepository.findAll().stream()
                .map(MeterReadingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MeterReadingResponseDTO> getSubmittedReadings() {
        return meterReadingRepository.findAllSubmittedReadings().stream()
                .map(MeterReadingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MeterReadingResponseDTO getMeterReadingById(Long id) {
        MeterReading meterReading = meterReadingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MeterReading", "id", id));
        return MeterReadingMapper.toDTO(meterReading);
    }

    @Transactional(readOnly = true)
    public List<MeterReadingResponseDTO> getMeterReadingsByMeterId(Long meterId) {
        return meterReadingRepository.findByMeterId(meterId).stream()
                .map(MeterReadingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MeterReadingResponseDTO> getMyMeterReadings(Long fieldStaffId) {
        return meterReadingRepository.findByFieldStaffId(fieldStaffId).stream()
                .map(MeterReadingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MeterReadingResponseDTO> getMeterReadingsByStatus(String status) {
        ReadingStatus readingStatus = ReadingStatus.valueOf(status);
        return meterReadingRepository.findByStatus(readingStatus).stream()
                .map(MeterReadingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MeterReadingResponseDTO getLatestReadingByMeterId(Long meterId) {
        MeterReading meterReading = meterReadingRepository.findLatestReadingByMeterId(meterId)
                .orElseThrow(() -> new ResourceNotFoundException("No readings found for meter"));
        return MeterReadingMapper.toDTO(meterReading);
    }

    @Transactional
    public MeterReadingResponseDTO verifyMeterReading(Long readingId, Long adminId) {
        MeterReading meterReading = meterReadingRepository.findById(readingId)
                .orElseThrow(() -> new ResourceNotFoundException("MeterReading", "id", readingId));

        User admin = userService.getUserEntityById(adminId);

        meterReading.setStatus(ReadingStatus.VERIFIED);
        meterReading.setVerifiedBy(admin);

        MeterReading updatedReading = meterReadingRepository.save(meterReading);
        return MeterReadingMapper.toDTO(updatedReading);
    }

    @Transactional
    public MeterReadingResponseDTO rejectMeterReading(Long readingId, Long adminId, String reason) {
        MeterReading meterReading = meterReadingRepository.findById(readingId)
                .orElseThrow(() -> new ResourceNotFoundException("MeterReading", "id", readingId));

        User admin = userService.getUserEntityById(adminId);

        meterReading.setStatus(ReadingStatus.REJECTED);
        meterReading.setVerifiedBy(admin);
        meterReading.setNotes(meterReading.getNotes() + "\nRejection Reason: " + reason);

        MeterReading updatedReading = meterReadingRepository.save(meterReading);
        return MeterReadingMapper.toDTO(updatedReading);
    }

    @Transactional
    public void deleteMeterReading(Long id) {
        MeterReading meterReading = meterReadingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MeterReading", "id", id));

        // Only REJECTED readings can be deleted
        if (meterReading.getStatus() != ReadingStatus.REJECTED) {
            throw new IllegalStateException("Only rejected readings can be deleted");
        }

        meterReadingRepository.delete(meterReading);
    }

    // Helper method to get MeterReading entity (for internal use)
    @Transactional(readOnly = true)
    public MeterReading getMeterReadingEntityById(Long id) {
        return meterReadingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MeterReading", "id", id));
    }
}