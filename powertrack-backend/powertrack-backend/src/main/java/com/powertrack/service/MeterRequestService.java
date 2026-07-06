package com.powertrack.service;

import com.powertrack.dto.mapper.MeterRequestMapper;
import com.powertrack.dto.request.meter.ApproveRejectMeterRequest;
import com.powertrack.dto.request.meter.MeterRequestDTO;
import com.powertrack.dto.response.meter.MeterRequestResponseDTO;
import com.powertrack.entity.Feeder;
import com.powertrack.entity.MeterRequest;
import com.powertrack.entity.User;
import com.powertrack.enums.MeterType;
import com.powertrack.enums.RequestStatus;
import com.powertrack.enums.UserRole;
import com.powertrack.exception.ResourceNotFoundException;
import com.powertrack.repository.MeterRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeterRequestService {

    private final MeterRequestRepository meterRequestRepository;
    private final UserService userService;
    private final FeederService feederService;

    @Transactional
    public MeterRequestResponseDTO createMeterRequest(MeterRequestDTO request, Long userId) {
        User user = userService.getUserEntityById(userId);
        Feeder feeder = feederService.getFeederEntityById(request.getFeederId());

        // Check if user has reached maximum meter limit (4 meters)
        long activeMeterCount = user.getMeters().stream()
                .filter(m -> m.getStatus().name().equals("ACTIVE"))
                .count();

        long pendingRequestCount = meterRequestRepository.countByuser_idAndStatus(userId , RequestStatus.PENDING);

        if (activeMeterCount + pendingRequestCount >= 4) {
            throw new IllegalStateException("Maximum meter limit (4) reached. Cannot request more meters.");
        }

        // Create meter request
        MeterRequest meterRequest = new MeterRequest();
        meterRequest.setUser(user);
        meterRequest.setMeterTypeRequested(MeterType.valueOf(request.getMeterType()));
        meterRequest.setConnectionAddress(request.getConnectionAddress());
        meterRequest.setFeeder(feeder);
        meterRequest.setReasonForRequest(request.getReasonForRequest());
        meterRequest.setStatus(RequestStatus.PENDING);

        MeterRequest savedRequest = meterRequestRepository.save(meterRequest);
        return MeterRequestMapper.toDTO(savedRequest);
    }

    @Transactional(readOnly = true)
    public List<MeterRequestResponseDTO> getMyMeterRequests(Long userId) {
        return meterRequestRepository.findByuser_id(userId).stream()
                .map(MeterRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MeterRequestResponseDTO> getAllPendingRequests() {
        return meterRequestRepository.findAllPendingRequests().stream()
                .map(MeterRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MeterRequestResponseDTO> getAllMeterRequests() {
        return meterRequestRepository.findAll().stream()
                .map(MeterRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MeterRequestResponseDTO getMeterRequestById(Long id) {
        MeterRequest meterRequest = meterRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MeterRequest", "id", id));
        return MeterRequestMapper.toDTO(meterRequest);
    }

    @Transactional(readOnly = true)
    public List<MeterRequestResponseDTO> getMeterRequestsByStatus(String status) {
        RequestStatus requestStatus = RequestStatus.valueOf(status);
        return meterRequestRepository.findByStatus(requestStatus).stream()
                .map(MeterRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MeterRequestResponseDTO> getAssignedMeterRequests(Long fieldStaffId) {
        return meterRequestRepository.findByAssignedFieldStaffId(fieldStaffId).stream()
                .map(MeterRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MeterRequestResponseDTO approveOrRejectMeterRequest(Long requestId,
                                                               ApproveRejectMeterRequest request,
                                                               Long adminId) {
        MeterRequest meterRequest = meterRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("MeterRequest", "id", requestId));

        User admin = userService.getUserEntityById(adminId);

        if (request.getAction().equals("APPROVE")) {
            // Validate assigned field staff
            if (request.getAssignedFieldStaffId() == null) {
                throw new IllegalArgumentException("Field staff must be assigned for approval");
            }

            User fieldStaff = userService.getUserEntityById(request.getAssignedFieldStaffId());

            if (fieldStaff.getRole() != UserRole.FIELD_STAFF) {
                throw new IllegalArgumentException("Assigned user must be a field staff");
            }

            meterRequest.setStatus(RequestStatus.APPROVED);
            meterRequest.setApprovedBy(admin);
            meterRequest.setApprovedDate(LocalDateTime.now());
            meterRequest.setAssignedFieldStaff(fieldStaff);
            meterRequest.setRejectionReason(null);

        } else if (request.getAction().equals("REJECT")) {
            // Validate rejection reason
            if (request.getRejectionReason() == null || request.getRejectionReason().trim().isEmpty()) {
                throw new IllegalArgumentException("Rejection reason is required");
            }

            meterRequest.setStatus(RequestStatus.REJECTED);
            meterRequest.setApprovedBy(admin);
            meterRequest.setApprovedDate(LocalDateTime.now());
            meterRequest.setRejectionReason(request.getRejectionReason());
            meterRequest.setAssignedFieldStaff(null);
        }

        MeterRequest updatedRequest = meterRequestRepository.save(meterRequest);
        return MeterRequestMapper.toDTO(updatedRequest);
    }

    @Transactional
    public void deleteMeterRequest(Long id) {
        MeterRequest meterRequest = meterRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MeterRequest", "id", id));

        // Only pending requests can be deleted
        if (meterRequest.getStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("Only pending requests can be deleted");
        }

        meterRequestRepository.delete(meterRequest);
    }

    @Transactional(readOnly = true)
    public long countPendingRequests() {
        return meterRequestRepository.countPendingRequests();
    }
}