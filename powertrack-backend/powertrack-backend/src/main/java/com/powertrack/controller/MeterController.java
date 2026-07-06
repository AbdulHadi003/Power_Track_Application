package com.powertrack.controller;

import com.powertrack.dto.response.meter.MeterResponseDTO;
import com.powertrack.service.MeterService;
import com.powertrack.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meters")
@RequiredArgsConstructor
public class MeterController {

    private final MeterService meterService;
    private final SecurityUtils securityUtils;

    @GetMapping("/my-meters")
    public ResponseEntity<List<MeterResponseDTO>> getMyMeters() {
        Long userId = securityUtils.getCurrentUserId();
        List<MeterResponseDTO> meters = meterService.getMyMeters(userId);
        return ResponseEntity.ok(meters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeterResponseDTO> getMeterById(@PathVariable Long id) {
        MeterResponseDTO meter = meterService.getMeterById(id);
        return ResponseEntity.ok(meter);
    }

    @GetMapping("/number/{meterNumber}")
    public ResponseEntity<MeterResponseDTO> getMeterByNumber(@PathVariable String meterNumber) {
        MeterResponseDTO meter = meterService.getMeterByNumber(meterNumber);
        return ResponseEntity.ok(meter);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MeterResponseDTO>> getAllMeters() {
        List<MeterResponseDTO> meters = meterService.getAllMeters();
        return ResponseEntity.ok(meters);
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MeterResponseDTO>> getActiveMeters() {
        List<MeterResponseDTO> meters = meterService.getActiveMeters();
        return ResponseEntity.ok(meters);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MeterResponseDTO>> getMetersByStatus(@PathVariable String status) {
        List<MeterResponseDTO> meters = meterService.getMetersByStatus(status);
        return ResponseEntity.ok(meters);
    }

    @GetMapping("/feeder/{feederId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MeterResponseDTO>> getMetersByFeeder(@PathVariable Long feederId) {
        List<MeterResponseDTO> meters = meterService.getMetersByFeeder(feederId);
        return ResponseEntity.ok(meters);
    }

    @GetMapping("/type/{meterType}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MeterResponseDTO>> getMetersByType(@PathVariable String meterType) {
        List<MeterResponseDTO> meters = meterService.getMetersByType(meterType);
        return ResponseEntity.ok(meters);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'FIELD_STAFF')")
    public ResponseEntity<MeterResponseDTO> updateMeterStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        MeterResponseDTO meter = meterService.updateMeterStatus(id, status);
        return ResponseEntity.ok(meter);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMeter(@PathVariable Long id) {
        meterService.deleteMeter(id);
        return ResponseEntity.ok("Meter deleted successfully");
    }

    @GetMapping("/count/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> countActiveMeters() {
        long count = meterService.countActiveMeters();
        return ResponseEntity.ok(count);
    }
}