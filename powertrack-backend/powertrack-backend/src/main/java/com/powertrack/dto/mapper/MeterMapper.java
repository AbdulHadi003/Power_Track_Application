package com.powertrack.dto.mapper;

import com.powertrack.dto.response.meter.MeterResponseDTO;
import com.powertrack.entity.Meter;
import org.springframework.stereotype.Component;

@Component
public class MeterMapper {

    public static MeterResponseDTO toDTO(Meter meter) {
        if (meter == null) {
            return null;
        }

        return MeterResponseDTO.builder()
                .id(meter.getId())
                .meterNumber(meter.getMeterNumber())
                .meterType(meter.getMeterType().name())
                .connectionAddress(meter.getConnectionAddress())
                .location(meter.getLocation())
                .feederId(meter.getFeederId())
                .feederName(meter.getFeeder() != null ? meter.getFeeder().getFeederName() : null)
                .feederCode(meter.getFeeder() != null ? meter.getFeeder().getFeederCode() : null)
                .area(meter.getFeeder() != null ? meter.getFeeder().getArea() : null)
                .status(meter.getStatus().name())
                .installationDate(meter.getInstallationDate())
                .lastReading(meter.getLastReading())
                .installedByName(meter.getInstalledBy() != null ? meter.getInstalledBy().getName() : null)
                .build();
    }
}