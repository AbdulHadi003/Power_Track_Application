package com.powertrack.controller;

import com.powertrack.dto.request.installment.ApproveRejectInstallmentRequest;
import com.powertrack.dto.request.installment.InstallmentRequestDTO;
import com.powertrack.dto.request.installment.PayInstallmentRequest;
import com.powertrack.dto.response.installment.InstallmentResponseDTO;
import com.powertrack.service.InstallmentService;
import com.powertrack.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/installments")
@RequiredArgsConstructor
public class InstallmentController {

    private final InstallmentService installmentService;
    private final SecurityUtils securityUtils;

    @PostMapping("/request")
    public ResponseEntity<InstallmentResponseDTO> requestInstallment(
            @Valid @RequestBody InstallmentRequestDTO request) {
        Long userId = securityUtils.getCurrentUserId();
        InstallmentResponseDTO response = installmentService.requestInstallment(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my-plans")
    public ResponseEntity<List<InstallmentResponseDTO>> getMyInstallmentRequests() {
        Long userId = securityUtils.getCurrentUserId();
        List<InstallmentResponseDTO> requests = installmentService.getMyInstallmentRequests(userId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstallmentResponseDTO> getInstallmentRequestById(@PathVariable Long id) {
        InstallmentResponseDTO request = installmentService.getInstallmentRequestById(id);
        return ResponseEntity.ok(request);
    }

    @PostMapping("/pay")
    public ResponseEntity<InstallmentResponseDTO> payInstallment(
            @Valid @RequestBody PayInstallmentRequest request) {
        InstallmentResponseDTO response = installmentService.payInstallment(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InstallmentResponseDTO>> getAllInstallmentRequests() {
        List<InstallmentResponseDTO> requests = installmentService.getAllInstallmentRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InstallmentResponseDTO>> getPendingInstallmentRequests() {
        List<InstallmentResponseDTO> requests = installmentService.getPendingInstallmentRequests();
        return ResponseEntity.ok(requests);
    }

    @PutMapping("/{id}/approve-reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InstallmentResponseDTO> approveOrRejectInstallment(
            @PathVariable Long id,
            @Valid @RequestBody ApproveRejectInstallmentRequest request) {
        Long adminId = securityUtils.getCurrentUserId();
        InstallmentResponseDTO response = installmentService.approveOrRejectInstallment(id, request, adminId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<String> deleteInstallmentRequest(@PathVariable Long id) {
        installmentService.deleteInstallmentRequest(id);
        return ResponseEntity.ok("Installment request deleted successfully");
    }

    @GetMapping("/count/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> countPendingInstallmentRequests() {
        long count = installmentService.countPendingInstallmentRequests();
        return ResponseEntity.ok(count);
    }
}