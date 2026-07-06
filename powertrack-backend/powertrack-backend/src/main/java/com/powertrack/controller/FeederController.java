package com.powertrack.controller;

import com.powertrack.dto.response.feeder.FeederResponseDTO;
import com.powertrack.service.FeederService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feeders")
@RequiredArgsConstructor
public class FeederController {

    private final FeederService feederService;

    @GetMapping
    public ResponseEntity<List<FeederResponseDTO>> getAllFeeders() {
        List<FeederResponseDTO> feeders = feederService.getAllFeeders();
        return ResponseEntity.ok(feeders);
    }

    @GetMapping("/active")
    public ResponseEntity<List<FeederResponseDTO>> getActiveFeeders() {
        List<FeederResponseDTO> feeders = feederService.getActiveFeeders();
        return ResponseEntity.ok(feeders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeederResponseDTO> getFeederById(@PathVariable Long id) {
        FeederResponseDTO feeder = feederService.getFeederById(id);
        return ResponseEntity.ok(feeder);
    }

    @GetMapping("/code/{feederCode}")
    public ResponseEntity<FeederResponseDTO> getFeederByCode(@PathVariable String feederCode) {
        FeederResponseDTO feeder = feederService.getFeederByCode(feederCode);
        return ResponseEntity.ok(feeder);
    }

    @GetMapping("/area/{area}")
    public ResponseEntity<List<FeederResponseDTO>> getFeedersByArea(@PathVariable String area) {
        List<FeederResponseDTO> feeders = feederService.getFeedersByArea(area);
        return ResponseEntity.ok(feeders);
    }

    @GetMapping("/substation/{substation}")
    public ResponseEntity<List<FeederResponseDTO>> getFeedersBySubstation(@PathVariable String substation) {
        List<FeederResponseDTO> feeders = feederService.getFeedersBySubstation(substation);
        return ResponseEntity.ok(feeders);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FeederResponseDTO>> getFeedersByStatus(@PathVariable String status) {
        List<FeederResponseDTO> feeders = feederService.getFeedersByStatus(status);
        return ResponseEntity.ok(feeders);
    }
}