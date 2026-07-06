package com.powertrack.controller;

import com.powertrack.dto.request.meter.ApproveRejectMeterRequest;
import com.powertrack.dto.request.meter.MeterRequestDTO;
import com.powertrack.dto.response.meter.MeterRequestResponseDTO;
import com.powertrack.service.MeterRequestService;
import com.powertrack.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meter-requests")
@RequiredArgsConstructor
public class MeterRequestController {

    private final MeterRequestService meterRequestService;
    private final SecurityUtils securityUtils;

    @PostMapping
    public ResponseEntity<MeterRequestResponseDTO> createMeterRequest(
            @Valid @RequestBody MeterRequestDTO request) {
        Long userId = securityUtils.getCurrentUserId();
        MeterRequestResponseDTO response = meterRequestService.createMeterRequest(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my-requests")
    public ResponseEntity<List<MeterRequestResponseDTO>> getMyMeterRequests() {
        Long userId = securityUtils.getCurrentUserId();
        List<MeterRequestResponseDTO> requests = meterRequestService.getMyMeterRequests(userId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeterRequestResponseDTO> getMeterRequestById(@PathVariable Long id) {
        MeterRequestResponseDTO request = meterRequestService.getMeterRequestById(id);
        return ResponseEntity.ok(request);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MeterRequestResponseDTO>> getAllMeterRequests() {
        List<MeterRequestResponseDTO> requests = meterRequestService.getAllMeterRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MeterRequestResponseDTO>> getPendingMeterRequests() {
        List<MeterRequestResponseDTO> requests = meterRequestService.getAllPendingRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MeterRequestResponseDTO>> getMeterRequestsByStatus(@PathVariable String status) {
        List<MeterRequestResponseDTO> requests = meterRequestService.getMeterRequestsByStatus(status);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/assigned-to-me")
    @PreAuthorize("hasRole('FIELD_STAFF')")
    public ResponseEntity<List<MeterRequestResponseDTO>> getAssignedMeterRequests() {
        Long fieldStaffId = securityUtils.getCurrentUserId();
        List<MeterRequestResponseDTO> requests = meterRequestService.getAssignedMeterRequests(fieldStaffId);
        return ResponseEntity.ok(requests);
    }

    @PutMapping("/{id}/approve-reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MeterRequestResponseDTO> approveOrRejectMeterRequest(
            @PathVariable Long id,
            @Valid @RequestBody ApproveRejectMeterRequest request) {
        Long adminId = securityUtils.getCurrentUserId();
        MeterRequestResponseDTO response = meterRequestService.approveOrRejectMeterRequest(id, request, adminId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMeterRequest(@PathVariable Long id) {
        meterRequestService.deleteMeterRequest(id);
        return ResponseEntity.ok("Meter request deleted successfully");
    }

    @GetMapping("/count/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> countPendingRequests() {
        long count = meterRequestService.countPendingRequests();
        return ResponseEntity.ok(count);
    }
}