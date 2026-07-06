package com.powertrack.dto.response.tariff;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TariffResponseDTO {

    private Long id;
    private String tariffName;
    private BigDecimal unitPrice;
    private Integer minUnits;
    private Integer maxUnits;
    private BigDecimal fixedCharge;
    private BigDecimal taxPercentage;
    private BigDecimal penaltyPercentage;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private Boolean isActive;
    private Long createdById;
    private String createdByName;
    private LocalDateTime createdAt;
}