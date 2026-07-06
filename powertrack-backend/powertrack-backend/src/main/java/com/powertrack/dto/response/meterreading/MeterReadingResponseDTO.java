package com.powertrack.dto.response.meterreading;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeterReadingResponseDTO {

    private Long id;
    private Long meterId;
    private String meterNumber;
    private Long fieldStaffId;
    private String fieldStaffName;
    private Integer previousReading;
    private Integer currentReading;
    private Integer unitsConsumed;
    private LocalDate readingDate;
    private String readingMonth;
    private Integer readingYear;
    private String readingType;
    private String photoUrl;
    private String notes;
    private String status;
    private Long verifiedById;
    private String verifiedByName;
    private LocalDateTime createdAt;
}