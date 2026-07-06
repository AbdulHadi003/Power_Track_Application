package com.powertrack.dto.mapper;

import com.powertrack.dto.request.tariff.TariffRequestDTO;
import com.powertrack.dto.response.tariff.TariffResponseDTO;
import com.powertrack.entity.Tariff;
import com.powertrack.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TariffMapper {

    public static Tariff toEntity(TariffRequestDTO dto, User createdBy) {
        Tariff tariff = new Tariff();
        tariff.setTariffName(dto.getTariffName());
        tariff.setUnitPrice(dto.getUnitPrice());
        tariff.setMinUnits(dto.getMinUnits());
        tariff.setMaxUnits(dto.getMaxUnits());
        tariff.setFixedCharge(dto.getFixedCharge());
        tariff.setTaxPercentage(dto.getTaxPercentage());
        tariff.setPenaltyPercentage(dto.getPenaltyPercentage());
        tariff.setEffectiveFrom(dto.getEffectiveFrom());
        tariff.setEffectiveTo(dto.getEffectiveTo());
        tariff.setIsActive(true);
        tariff.setCreatedBy(createdBy);
        return tariff;
    }

    public static TariffResponseDTO toDTO(Tariff tariff) {
        if (tariff == null) {
            return null;
        }

        return TariffResponseDTO.builder()
                .id(tariff.getId())
                .tariffName(tariff.getTariffName())
                .unitPrice(tariff.getUnitPrice())
                .minUnits(tariff.getMinUnits())
                .maxUnits(tariff.getMaxUnits())
                .fixedCharge(tariff.getFixedCharge())
                .taxPercentage(tariff.getTaxPercentage())
                .penaltyPercentage(tariff.getPenaltyPercentage())
                .effectiveFrom(tariff.getEffectiveFrom())
                .effectiveTo(tariff.getEffectiveTo())
                .isActive(tariff.getIsActive())
                .createdById(tariff.getCreatedById())
                .createdByName(tariff.getCreatedBy() != null ? tariff.getCreatedBy().getName() : null)
                .createdAt(tariff.getCreatedAt())
                .build();
    }

    public static void updateEntity(Tariff tariff, TariffRequestDTO dto) {
        tariff.setTariffName(dto.getTariffName());
        tariff.setUnitPrice(dto.getUnitPrice());
        tariff.setMinUnits(dto.getMinUnits());
        tariff.setMaxUnits(dto.getMaxUnits());
        tariff.setFixedCharge(dto.getFixedCharge());
        tariff.setTaxPercentage(dto.getTaxPercentage());
        tariff.setPenaltyPercentage(dto.getPenaltyPercentage());
        tariff.setEffectiveFrom(dto.getEffectiveFrom());
        tariff.setEffectiveTo(dto.getEffectiveTo());
    }
}