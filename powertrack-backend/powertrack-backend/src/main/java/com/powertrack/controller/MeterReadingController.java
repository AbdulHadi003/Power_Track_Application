package com.powertrack.controller;

import com.powertrack.dto.request.meterreading.MeterReadingRequestDTO;
import com.powertrack.dto.response.meterreading.MeterReadingResponseDTO;
import com.powertrack.service.MeterReadingService;
import com.powertrack.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meter-readings")
@RequiredArgsConstructor
public class MeterReadingController {

    private final MeterReadingService meterReadingService;
    private final SecurityUtils securityUtils;

    @PostMapping("/submit")
    @PreAuthorize("hasRole('FIELD_STAFF')")
    public ResponseEntity<MeterReadingResponseDTO> submitMeterReading(
            @Valid @RequestBody MeterReadingRequestDTO request) {
        Long fieldStaffId = securityUtils.getCurrentUserId();
        MeterReadingResponseDTO response = meterReadingService.submitMeterReading(request, fieldStaffId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my-readings")
    @PreAuthorize("hasRole('FIELD_STAFF')")
    public ResponseEntity<List<MeterReadingResponseDTO>> getMyMeterReadings() {
        Long fieldStaffId = securityUtils.getCurrentUserId();
        List<MeterReadingResponseDTO> readings = meterReadingService.getMyMeterReadings(fieldStaffId);
        return ResponseEntity.ok(readings);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FIELD_STAFF')")
    public ResponseEntity<MeterReadingResponseDTO> getMeterReadingById(@PathVariable Long id) {
        MeterReadingResponseDTO reading = meterReadingService.getMeterReadingById(id);
        return ResponseEntity.ok(reading);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MeterReadingResponseDTO>> getAllMeterReadings() {
        List<MeterReadingResponseDTO> readings = meterReadingService.getAllMeterReadings();
        return ResponseEntity.ok(readings);
    }

    @GetMapping("/submitted")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MeterReadingResponseDTO>> getSubmittedReadings() {
        List<MeterReadingResponseDTO> readings = meterReadingService.getSubmittedReadings();
        return ResponseEntity.ok(readings);
    }

    @GetMapping("/meter/{meterId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MeterReadingResponseDTO>> getMeterReadingsByMeterId(@PathVariable Long meterId) {
        List<MeterReadingResponseDTO> readings = meterReadingService.getMeterReadingsByMeterId(meterId);
        return ResponseEntity.ok(readings);
    }

    @GetMapping("/meter/{meterId}/latest")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MeterReadingResponseDTO> getLatestReadingByMeterId(@PathVariable Long meterId) {
        MeterReadingResponseDTO reading = meterReadingService.getLatestReadingByMeterId(meterId);
        return ResponseEntity.ok(reading);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MeterReadingResponseDTO>> getMeterReadingsByStatus(@PathVariable String status) {
        List<MeterReadingResponseDTO> readings = meterReadingService.getMeterReadingsByStatus(status);
        return ResponseEntity.ok(readings);
    }

    @PutMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MeterReadingResponseDTO> verifyMeterReading(@PathVariable Long id) {
        Long adminId = securityUtils.getCurrentUserId();
        MeterReadingResponseDTO reading = meterReadingService.verifyMeterReading(id, adminId);
        return ResponseEntity.ok(reading);
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MeterReadingResponseDTO> rejectMeterReading(
            @PathVariable Long id,
            @RequestParam String reason) {
        Long adminId = securityUtils.getCurrentUserId();
        MeterReadingResponseDTO reading = meterReadingService.rejectMeterReading(id, adminId, reason);
        return ResponseEntity.ok(reading);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMeterReading(@PathVariable Long id) {
        meterReadingService.deleteMeterReading(id);
        return ResponseEntity.ok("Meter reading deleted successfully");
    }
}