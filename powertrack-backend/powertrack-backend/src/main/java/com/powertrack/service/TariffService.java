package com.powertrack.service;

import com.powertrack.dto.mapper.TariffMapper;
import com.powertrack.dto.request.tariff.TariffRequestDTO;
import com.powertrack.dto.response.tariff.TariffResponseDTO;
import com.powertrack.entity.Tariff;
import com.powertrack.entity.User;
import com.powertrack.exception.ResourceNotFoundException;
import com.powertrack.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TariffService {

    private final TariffRepository tariffRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<TariffResponseDTO> getAllTariffs() {
        return tariffRepository.findAll().stream()
                .map(TariffMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TariffResponseDTO> getActiveTariffs() {
        return tariffRepository.findByIsActiveTrue().stream()
                .map(TariffMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TariffResponseDTO getCurrentActiveTariff() {
        Tariff tariff = tariffRepository.findCurrentActiveTariff()
                .orElseThrow(() -> new ResourceNotFoundException("No active tariff found"));
        return TariffMapper.toDTO(tariff);
    }

    @Transactional(readOnly = true)
    public TariffResponseDTO getTariffById(Long id) {
        Tariff tariff = tariffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tariff", "id", id));
        return TariffMapper.toDTO(tariff);
    }

    @Transactional
    public TariffResponseDTO createTariff(TariffRequestDTO request, Long adminId) {
        // Check if tariff name already exists
        if (tariffRepository.existsByTariffName(request.getTariffName())) {
            throw new IllegalArgumentException("Tariff with this name already exists");
        }

        // Get admin user
        User admin = userService.getUserEntityById(adminId);

        // Create tariff
        Tariff tariff = new Tariff();
        tariff.setTariffName(request.getTariffName());
        tariff.setUnitPrice(request.getUnitPrice());
        tariff.setMinUnits(request.getMinUnits());
        tariff.setMaxUnits(request.getMaxUnits());
        tariff.setFixedCharge(request.getFixedCharge());
        tariff.setTaxPercentage(request.getTaxPercentage());
        tariff.setPenaltyPercentage(request.getPenaltyPercentage());
        tariff.setEffectiveFrom(request.getEffectiveFrom());
        tariff.setEffectiveTo(request.getEffectiveTo());
        tariff.setIsActive(true);
        tariff.setCreatedBy(admin);

        Tariff savedTariff = tariffRepository.save(tariff);
        return TariffMapper.toDTO(savedTariff);
    }

    @Transactional
    public TariffResponseDTO updateTariff(Long id, TariffRequestDTO request) {
        Tariff tariff = tariffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tariff", "id", id));

        tariff.setTariffName(request.getTariffName());
        tariff.setUnitPrice(request.getUnitPrice());
        tariff.setMinUnits(request.getMinUnits());
        tariff.setMaxUnits(request.getMaxUnits());
        tariff.setFixedCharge(request.getFixedCharge());
        tariff.setTaxPercentage(request.getTaxPercentage());
        tariff.setPenaltyPercentage(request.getPenaltyPercentage());
        tariff.setEffectiveFrom(request.getEffectiveFrom());
        tariff.setEffectiveTo(request.getEffectiveTo());

        Tariff updatedTariff = tariffRepository.save(tariff);
        return TariffMapper.toDTO(updatedTariff);
    }

    @Transactional
    public void deactivateTariff(Long id) {
        Tariff tariff = tariffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tariff", "id", id));

        tariff.setIsActive(false);
        tariffRepository.save(tariff);
    }

    @Transactional
    public void activateTariff(Long id) {
        Tariff tariff = tariffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tariff", "id", id));

        tariff.setIsActive(true);
        tariffRepository.save(tariff);
    }

    @Transactional
    public void deleteTariff(Long id) {
        Tariff tariff = tariffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tariff", "id", id));

        tariffRepository.delete(tariff);
    }

    // Helper method to get current active tariff entity
    @Transactional(readOnly = true)
    public Tariff getCurrentActiveTariffEntity() {
        return tariffRepository.findCurrentActiveTariff()
                .orElseThrow(() -> new ResourceNotFoundException("No active tariff found"));
    }
}