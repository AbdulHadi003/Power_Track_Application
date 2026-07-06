package com.powertrack.controller;

import com.powertrack.dto.request.tariff.TariffRequestDTO;
import com.powertrack.dto.response.tariff.TariffResponseDTO;
import com.powertrack.service.TariffService;
import com.powertrack.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tariffs")
@RequiredArgsConstructor
public class TariffController {

    private final TariffService tariffService;
    private final SecurityUtils securityUtils;

    @GetMapping
    public ResponseEntity<List<TariffResponseDTO>> getAllTariffs() {
        List<TariffResponseDTO> tariffs = tariffService.getAllTariffs();
        return ResponseEntity.ok(tariffs);
    }

    @GetMapping("/active")
    public ResponseEntity<List<TariffResponseDTO>> getActiveTariffs() {
        List<TariffResponseDTO> tariffs = tariffService.getActiveTariffs();
        return ResponseEntity.ok(tariffs);
    }

    @GetMapping("/current")
    public ResponseEntity<TariffResponseDTO> getCurrentActiveTariff() {
        TariffResponseDTO tariff = tariffService.getCurrentActiveTariff();
        return ResponseEntity.ok(tariff);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TariffResponseDTO> getTariffById(@PathVariable Long id) {
        TariffResponseDTO tariff = tariffService.getTariffById(id);
        return ResponseEntity.ok(tariff);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TariffResponseDTO> createTariff(@Valid @RequestBody TariffRequestDTO request) {
        Long adminId = securityUtils.getCurrentUserId();
        TariffResponseDTO tariff = tariffService.createTariff(request, adminId);
        return ResponseEntity.status(HttpStatus.CREATED).body(tariff);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TariffResponseDTO> updateTariff(
            @PathVariable Long id,
            @Valid @RequestBody TariffRequestDTO request) {
        TariffResponseDTO tariff = tariffService.updateTariff(id, request);
        return ResponseEntity.ok(tariff);
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deactivateTariff(@PathVariable Long id) {
        tariffService.deactivateTariff(id);
        return ResponseEntity.ok("Tariff deactivated successfully");
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> activateTariff(@PathVariable Long id) {
        tariffService.activateTariff(id);
        return ResponseEntity.ok("Tariff activated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteTariff(@PathVariable Long id) {
        tariffService.deleteTariff(id);
        return ResponseEntity.ok("Tariff deleted successfully");
    }
}