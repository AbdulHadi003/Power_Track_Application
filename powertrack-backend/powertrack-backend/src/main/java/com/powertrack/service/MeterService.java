package com.powertrack.service;

import com.powertrack.dto.mapper.MeterMapper;
import com.powertrack.dto.response.meter.MeterResponseDTO;
import com.powertrack.entity.Feeder;
import com.powertrack.entity.Meter;
import com.powertrack.entity.User;
import com.powertrack.enums.MeterStatus;
import com.powertrack.enums.MeterType;
import com.powertrack.exception.ResourceNotFoundException;
import com.powertrack.repository.MeterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeterService {

    private final MeterRepository meterRepository;
    private final UserService userService;
    private final FeederService feederService;

    @Transactional(readOnly = true)
    public List<MeterResponseDTO> getAllMeters() {
        return meterRepository.findAll().stream()
                .map(MeterMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MeterResponseDTO> getActiveMeters() {
        return meterRepository.findAllActiveMeters().stream()
                .map(MeterMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MeterResponseDTO getMeterById(Long id) {
        Meter meter = meterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meter", "id", id));
        return MeterMapper.toDTO(meter);
    }

    @Transactional(readOnly = true)
    public MeterResponseDTO getMeterByNumber(String meterNumber) {
        Meter meter = meterRepository.findByMeterNumber(meterNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Meter", "meterNumber", meterNumber));
        return MeterMapper.toDTO(meter);
    }

    @Transactional(readOnly = true)
    public List<MeterResponseDTO> getMyMeters(Long userId) {
        return meterRepository.findByUserId(userId).stream()
                .map(MeterMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MeterResponseDTO> getMetersByStatus(String status) {
        MeterStatus meterStatus = MeterStatus.valueOf(status);
        return meterRepository.findByStatus(meterStatus).stream()
                .map(MeterMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MeterResponseDTO> getMetersByFeeder(Long feederId) {
        return meterRepository.findByFeederId(feederId).stream()
                .map(MeterMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MeterResponseDTO> getMetersByType(String meterType) {
        MeterType type = MeterType.valueOf(meterType);
        return meterRepository.findByMeterType(type).stream()
                .map(MeterMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MeterResponseDTO createMeter(Long userId, Long feederId, MeterType meterType,
                                        String connectionAddress, Long installedById, Long verifiedById) {
        User user = userService.getUserEntityById(userId);
        Feeder feeder = feederService.getFeederEntityById(feederId);

        // Check meter limit (4 meters per user)
        long activeMeterCount = meterRepository.countByUserIdAndStatus(userId, MeterStatus.ACTIVE);
        if (activeMeterCount >= 4) {
            throw new IllegalStateException("User has reached maximum meter limit (4)");
        }

        // Create meter
        Meter meter = new Meter();
        meter.setUser(user);
        meter.setFeeder(feeder);
        meter.setMeterNumber(generateMeterNumber());
        meter.setMeterType(meterType);
        meter.setConnectionAddress(connectionAddress);
        meter.setInstallationDate(LocalDate.now());
        meter.setStatus(MeterStatus.ACTIVE);
        meter.setLastReading(0);

        if (installedById != null) {
            User installedBy = userService.getUserEntityById(installedById);
            meter.setInstalledBy(installedBy);
        }

        if (verifiedById != null) {
            User verifiedBy = userService.getUserEntityById(verifiedById);
            meter.setInstallationVerifiedBy(verifiedBy);
        }

        Meter savedMeter = meterRepository.save(meter);
        return MeterMapper.toDTO(savedMeter);
    }

    @Transactional
    public MeterResponseDTO updateMeterStatus(Long id, String status) {
        Meter meter = meterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meter", "id", id));

        MeterStatus meterStatus = MeterStatus.valueOf(status);
        meter.setStatus(meterStatus);

        Meter updatedMeter = meterRepository.save(meter);
        return MeterMapper.toDTO(updatedMeter);
    }

    @Transactional
    public MeterResponseDTO updateLastReading(Long id, Integer lastReading) {
        Meter meter = meterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meter", "id", id));

        meter.setLastReading(lastReading);
        Meter updatedMeter = meterRepository.save(meter);
        return MeterMapper.toDTO(updatedMeter);
    }

    @Transactional
    public void deleteMeter(Long id) {
        Meter meter = meterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meter", "id", id));

        // Can only delete INACTIVE or FAULTY meters
        if (meter.getStatus() == MeterStatus.ACTIVE) {
            throw new IllegalStateException("Cannot delete ACTIVE meter. Deactivate it first.");
        }

        meterRepository.delete(meter);
    }

    @Transactional(readOnly = true)
    public long countActiveMeters() {
        return meterRepository.countActiveMeters();
    }

    // Helper method to generate unique meter number
    private String generateMeterNumber() {
        String meterNumber;
        do {
            meterNumber = "MTR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (meterRepository.existsByMeterNumber(meterNumber));
        return meterNumber;
    }

    // Helper method to get Meter entity (for internal use)
    @Transactional(readOnly = true)
    public Meter getMeterEntityById(Long id) {
        return meterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meter", "id", id));
    }
}