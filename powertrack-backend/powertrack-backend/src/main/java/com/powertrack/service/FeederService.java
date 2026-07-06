package com.powertrack.service;

import com.powertrack.dto.mapper.FeederMapper;
import com.powertrack.dto.response.feeder.FeederResponseDTO;
import com.powertrack.entity.Feeder;
import com.powertrack.enums.FeederStatus;
import com.powertrack.exception.ResourceNotFoundException;
import com.powertrack.repository.FeederRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeederService {

    private final FeederRepository feederRepository;

    @Transactional(readOnly = true)
    public List<FeederResponseDTO> getAllFeeders() {
        return feederRepository.findAll().stream()
                .map(FeederMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FeederResponseDTO> getActiveFeeders() {
        return feederRepository.findAllActiveFeeders().stream()
                .map(FeederMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FeederResponseDTO getFeederById(Long id) {
        Feeder feeder = feederRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feeder", "id", id));
        return FeederMapper.toDTO(feeder);
    }

    @Transactional(readOnly = true)
    public FeederResponseDTO getFeederByCode(String feederCode) {
        Feeder feeder = feederRepository.findByFeederCode(feederCode)
                .orElseThrow(() -> new ResourceNotFoundException("Feeder", "feederCode", feederCode));
        return FeederMapper.toDTO(feeder);
    }

    @Transactional(readOnly = true)
    public List<FeederResponseDTO> getFeedersByArea(String area) {
        return feederRepository.findByAreaContainingIgnoreCase(area).stream()
                .map(FeederMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FeederResponseDTO> getFeedersBySubstation(String substation) {
        return feederRepository.findBySubstation(substation).stream()
                .map(FeederMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FeederResponseDTO> getFeedersByStatus(String status) {
        FeederStatus feederStatus = FeederStatus.valueOf(status);
        return feederRepository.findByStatus(feederStatus).stream()
                .map(FeederMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Helper method to get Feeder entity (for internal use by other services)
    @Transactional(readOnly = true)
    public Feeder getFeederEntityById(Long id) {
        return feederRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feeder", "id", id));
    }
}