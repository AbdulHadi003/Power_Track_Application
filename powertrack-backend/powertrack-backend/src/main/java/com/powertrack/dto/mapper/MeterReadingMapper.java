package com.powertrack.dto.mapper;


import com.powertrack.dto.request.meterreading.MeterReadingRequestDTO;
import com.powertrack.dto.response.meterreading.MeterReadingResponseDTO;
import com.powertrack.entity.Meter;
import com.powertrack.entity.MeterReading;
import com.powertrack.entity.User;
import com.powertrack.enums.ReadingStatus;
import com.powertrack.enums.ReadingType;
import org.springframework.stereotype.Component;

@Component
public class MeterReadingMapper {

    public static MeterReading toEntity(MeterReadingRequestDTO dto, Meter meter, User fieldStaff) {
        MeterReading reading = new MeterReading();
        reading.setMeter(meter);
        reading.setFieldStaff(fieldStaff);
        reading.setPreviousReading(meter.getLastReading() != null ? meter.getLastReading() : 0);
        reading.setCurrentReading(dto.getCurrentReading());
        reading.setUnitsConsumed(dto.getCurrentReading() - (meter.getLastReading() != null ? meter.getLastReading() : 0));
        reading.setReadingDate(dto.getReadingDate());
        reading.setReadingMonth(dto.getReadingMonth());
        reading.setReadingYear(dto.getReadingYear());
        reading.setReadingType(ReadingType.valueOf(dto.getReadingType()));
        reading.setPhotoUrl(dto.getPhotoUrl());
        reading.setNotes(dto.getNotes());
        reading.setStatus(ReadingStatus.SUBMITTED);
        return reading;
    }

    public static MeterReadingResponseDTO toDTO(MeterReading reading) {
        if (reading == null) {
            return null;
        }

        return MeterReadingResponseDTO.builder()
                .id(reading.getId())
                .meterId(reading.getMeterId())
                .meterNumber(reading.getMeter() != null ? reading.getMeter().getMeterNumber() : null)
                .fieldStaffId(reading.getFieldStaffId())
                .fieldStaffName(reading.getFieldStaff() != null ? reading.getFieldStaff().getName() : null)
                .previousReading(reading.getPreviousReading())
                .currentReading(reading.getCurrentReading())
                .unitsConsumed(reading.getUnitsConsumed())
                .readingDate(reading.getReadingDate())
                .readingMonth(reading.getReadingMonth())
                .readingYear(reading.getReadingYear())
                .readingType(reading.getReadingType().name())
                .photoUrl(reading.getPhotoUrl())
                .notes(reading.getNotes())
                .status(reading.getStatus().name())
                .verifiedById(reading.getVerifiedById())
                .verifiedByName(reading.getVerifiedBy() != null ? reading.getVerifiedBy().getName() : null)
                .createdAt(reading.getCreatedAt())
                .build();
    }
}