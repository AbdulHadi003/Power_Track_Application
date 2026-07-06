package com.powertrack.service;

import com.powertrack.dto.mapper.ComplaintMapper;
import com.powertrack.dto.request.complaint.ComplaintRequestDTO;
import com.powertrack.dto.request.complaint.UpdateComplaintRequest;
import com.powertrack.dto.response.complaint.ComplaintResponseDTO;
import com.powertrack.entity.Complaint;
import com.powertrack.entity.Meter;
import com.powertrack.entity.User;
import com.powertrack.enums.ComplaintCategory;
import com.powertrack.enums.ComplaintPriority;
import com.powertrack.enums.ComplaintStatus;
import com.powertrack.exception.ResourceNotFoundException;
import com.powertrack.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserService userService;
    private final MeterService meterService;

    @Transactional
    public ComplaintResponseDTO createComplaint(ComplaintRequestDTO request, Long userId) {
        User user = userService.getUserEntityById(userId);

        // Create complaint
        Complaint complaint = new Complaint();
        complaint.setUser(user);
        complaint.setSubject(request.getSubject());
        complaint.setDescription(request.getDescription());
        complaint.setCategory(ComplaintCategory.valueOf(request.getCategory()));
        complaint.setStatus(ComplaintStatus.NEW);
        complaint.setPriority(ComplaintPriority.MEDIUM);
        complaint.setAttachmentUrl(request.getAttachmentUrl());

        // Link to meter if provided
        if (request.getMeterId() != null) {
            Meter meter = meterService.getMeterEntityById(request.getMeterId());
            complaint.setMeter(meter);
        }

        Complaint savedComplaint = complaintRepository.save(complaint);
        return ComplaintMapper.toDTO(savedComplaint);
    }

    @Transactional(readOnly = true)
    public List<ComplaintResponseDTO> getAllComplaints() {
        return complaintRepository.findAll().stream()
                .map(ComplaintMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ComplaintResponseDTO getComplaintById(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint", "id", id));
        return ComplaintMapper.toDTO(complaint);
    }

    @Transactional(readOnly = true)
    public ComplaintResponseDTO getComplaintByToken(String token) {
        Complaint complaint = complaintRepository.findByComplaintToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint", "token", token));
        return ComplaintMapper.toDTO(complaint);
    }

    @Transactional(readOnly = true)
    public List<ComplaintResponseDTO> getMyComplaints(Long userId) {
        return complaintRepository.findByUserId(userId).stream()
                .map(ComplaintMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ComplaintResponseDTO> getComplaintsByStatus(String status) {
        ComplaintStatus complaintStatus = ComplaintStatus.valueOf(status);
        return complaintRepository.findByStatus(complaintStatus).stream()
                .map(ComplaintMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ComplaintResponseDTO> getNewComplaints() {
        return complaintRepository.findNewComplaints().stream()
                .map(ComplaintMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ComplaintResponseDTO> getPendingComplaints() {
        return complaintRepository.findPendingComplaints().stream()
                .map(ComplaintMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ComplaintResponseDTO> getMyAssignedComplaints(Long csrId) {
        return complaintRepository.findByHandledById(csrId).stream()
                .map(ComplaintMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ComplaintResponseDTO> getComplaintsByCategory(String category) {
        ComplaintCategory complaintCategory = ComplaintCategory.valueOf(category);
        return complaintRepository.findByCategory(complaintCategory).stream()
                .map(ComplaintMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ComplaintResponseDTO updateComplaintStatus(Long complaintId,
                                                      UpdateComplaintRequest request,
                                                      Long handlerId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint", "id", complaintId));

        User handler = userService.getUserEntityById(handlerId);

        // Update status
        ComplaintStatus newStatus = ComplaintStatus.valueOf(request.getStatus());
        complaint.setStatus(newStatus);

        // Set handler if not already set
        if (complaint.getHandledBy() == null) {
            complaint.setHandledBy(handler);
        }

        // Update resolution notes
        if (request.getResolutionNotes() != null) {
            complaint.setResolutionNotes(request.getResolutionNotes());
        }

        // Update priority if provided
        if (request.getPriority() != null) {
            ComplaintPriority priority = ComplaintPriority.valueOf(request.getPriority());
            complaint.setPriority(priority);
        }

        // Set resolved date if resolved
        if (newStatus == ComplaintStatus.RESOLVED || newStatus == ComplaintStatus.CLOSED) {
            if (complaint.getResolvedAt() == null) {
                complaint.setResolvedAt(LocalDateTime.now());
            }
        }

        Complaint updatedComplaint = complaintRepository.save(complaint);
        return ComplaintMapper.toDTO(updatedComplaint);
    }

    @Transactional
    public ComplaintResponseDTO forwardComplaintToAdmin(Long complaintId, Long adminId, Long csrId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint", "id", complaintId));

        User admin = userService.getUserEntityById(adminId);
        User csr = userService.getUserEntityById(csrId);

        complaint.setStatus(ComplaintStatus.FORWARDED);
        complaint.setHandledBy(csr);
        complaint.setForwardedTo(admin);

        Complaint updatedComplaint = complaintRepository.save(complaint);
        return ComplaintMapper.toDTO(updatedComplaint);
    }

    @Transactional
    public ComplaintResponseDTO assignComplaintToCSR(Long complaintId, Long csrId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint", "id", complaintId));

        User csr = userService.getUserEntityById(csrId);

        complaint.setHandledBy(csr);
        complaint.setStatus(ComplaintStatus.IN_PROGRESS);

        Complaint updatedComplaint = complaintRepository.save(complaint);
        return ComplaintMapper.toDTO(updatedComplaint);
    }

    @Transactional
    public void deleteComplaint(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint", "id", id));

        complaintRepository.delete(complaint);
    }

    @Transactional(readOnly = true)
    public long countComplaintsByStatus(String status) {
        ComplaintStatus complaintStatus = ComplaintStatus.valueOf(status);
        return complaintRepository.countByStatus(complaintStatus);
    }

    @Transactional(readOnly = true)
    public long countNewComplaints() {
        return complaintRepository.countNewComplaints();
    }

    @Transactional(readOnly = true)
    public long countPendingComplaints() {
        return complaintRepository.countPendingComplaints();
    }
}