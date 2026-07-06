package com.powertrack.dto.request.tariff;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TariffRequestDTO {

    @NotBlank(message = "Tariff name is required")
    @Size(max = 100, message = "Tariff name cannot exceed 100 characters")
    private String tariffName;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.01", message = "Unit price must be greater than 0")
    private BigDecimal unitPrice;

    @NotNull(message = "Minimum units is required")
    @Min(value = 0, message = "Minimum units cannot be negative")
    private Integer minUnits;

    @Min(value = 0, message = "Maximum units cannot be negative")
    private Integer maxUnits; // Optional - null means unlimited

    @NotNull(message = "Fixed charge is required")
    @DecimalMin(value = "0.00", message = "Fixed charge cannot be negative")
    private BigDecimal fixedCharge;

    @NotNull(message = "Tax percentage is required")
    @DecimalMin(value = "0.00", message = "Tax percentage cannot be negative")
    @DecimalMax(value = "100.00", message = "Tax percentage cannot exceed 100")
    private BigDecimal taxPercentage;

    @NotNull(message = "Penalty percentage is required")
    @DecimalMin(value = "0.00", message = "Penalty percentage cannot be negative")
    @DecimalMax(value = "100.00", message = "Penalty percentage cannot exceed 100")
    private BigDecimal penaltyPercentage;

    @NotNull(message = "Effective from date is required")
    private LocalDate effectiveFrom;

    private LocalDate effectiveTo; // Optional - null means no end date
}