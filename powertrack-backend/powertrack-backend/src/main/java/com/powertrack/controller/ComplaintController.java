package com.powertrack.controller;

import com.powertrack.dto.request.complaint.ComplaintRequestDTO;
import com.powertrack.dto.request.complaint.UpdateComplaintRequest;
import com.powertrack.dto.response.complaint.ComplaintResponseDTO;
import com.powertrack.service.ComplaintService;
import com.powertrack.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;
    private final SecurityUtils securityUtils;

    @PostMapping
    public ResponseEntity<ComplaintResponseDTO> createComplaint(
            @Valid @RequestBody ComplaintRequestDTO request) {
        Long userId = securityUtils.getCurrentUserId();
        ComplaintResponseDTO complaint = complaintService.createComplaint(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(complaint);
    }

    @GetMapping("/my-complaints")
    public ResponseEntity<List<ComplaintResponseDTO>> getMyComplaints() {
        Long userId = securityUtils.getCurrentUserId();
        List<ComplaintResponseDTO> complaints = complaintService.getMyComplaints(userId);
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplaintResponseDTO> getComplaintById(@PathVariable Long id) {
        ComplaintResponseDTO complaint = complaintService.getComplaintById(id);
        return ResponseEntity.ok(complaint);
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<ComplaintResponseDTO> getComplaintByToken(@PathVariable String token) {
        ComplaintResponseDTO complaint = complaintService.getComplaintByToken(token);
        return ResponseEntity.ok(complaint);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public ResponseEntity<List<ComplaintResponseDTO>> getAllComplaints() {
        List<ComplaintResponseDTO> complaints = complaintService.getAllComplaints();
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public ResponseEntity<List<ComplaintResponseDTO>> getNewComplaints() {
        List<ComplaintResponseDTO> complaints = complaintService.getNewComplaints();
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public ResponseEntity<List<ComplaintResponseDTO>> getPendingComplaints() {
        List<ComplaintResponseDTO> complaints = complaintService.getPendingComplaints();
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public ResponseEntity<List<ComplaintResponseDTO>> getComplaintsByStatus(@PathVariable String status) {
        List<ComplaintResponseDTO> complaints = complaintService.getComplaintsByStatus(status);
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public ResponseEntity<List<ComplaintResponseDTO>> getComplaintsByCategory(@PathVariable String category) {
        List<ComplaintResponseDTO> complaints = complaintService.getComplaintsByCategory(category);
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/assigned-to-me")
    @PreAuthorize("hasRole('SUPPORT')")
    public ResponseEntity<List<ComplaintResponseDTO>> getMyAssignedComplaints() {
        Long csrId = securityUtils.getCurrentUserId();
        List<ComplaintResponseDTO> complaints = complaintService.getMyAssignedComplaints(csrId);
        return ResponseEntity.ok(complaints);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public ResponseEntity<ComplaintResponseDTO> updateComplaintStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateComplaintRequest request) {
        Long handlerId = securityUtils.getCurrentUserId();
        ComplaintResponseDTO complaint = complaintService.updateComplaintStatus(id, request, handlerId);
        return ResponseEntity.ok(complaint);
    }

    @PutMapping("/{id}/forward")
    @PreAuthorize("hasRole('SUPPORT')")
    public ResponseEntity<ComplaintResponseDTO> forwardComplaintToAdmin(
            @PathVariable Long id,
            @RequestParam Long adminId) {
        Long csrId = securityUtils.getCurrentUserId();
        ComplaintResponseDTO complaint = complaintService.forwardComplaintToAdmin(id, adminId, csrId);
        return ResponseEntity.ok(complaint);
    }

    @PutMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ComplaintResponseDTO> assignComplaintToCSR(
            @PathVariable Long id,
            @RequestParam Long csrId) {
        ComplaintResponseDTO complaint = complaintService.assignComplaintToCSR(id, csrId);
        return ResponseEntity.ok(complaint);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteComplaint(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
        return ResponseEntity.ok("Complaint deleted successfully");
    }

    @GetMapping("/count/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public ResponseEntity<Long> countComplaintsByStatus(@PathVariable String status) {
        long count = complaintService.countComplaintsByStatus(status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public ResponseEntity<Long> countNewComplaints() {
        long count = complaintService.countNewComplaints();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public ResponseEntity<Long> countPendingComplaints() {
        long count = complaintService.countPendingComplaints();
        return ResponseEntity.ok(count);
    }
}