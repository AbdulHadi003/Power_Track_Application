package com.powertrack.dto.mapper;

import com.powertrack.dto.response.feeder.FeederResponseDTO;
import com.powertrack.entity.Feeder;
import org.springframework.stereotype.Component;

@Component
public class FeederMapper {

    public static FeederResponseDTO toDTO(Feeder feeder) {
        if (feeder == null) {
            return null;
        }

        return FeederResponseDTO.builder()
                .id(feeder.getId())
                .feederCode(feeder.getFeederCode())
                .feederName(feeder.getFeederName())
                .area(feeder.getArea())
                .substation(feeder.getSubstation())
                .capacityKw(feeder.getCapacityKw())
                .status(feeder.getStatus().name())
                .createdAt(feeder.getCreatedAt())
                .build();
    }
}