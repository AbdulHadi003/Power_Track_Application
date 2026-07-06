package com.powertrack.dto.request.bill;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateBillRequest {

    @NotNull(message = "Meter ID is required")
    private Long meterId;

    @NotNull(message = "Units consumed is required")
    @Min(value = 0, message = "Units consumed cannot be negative")
    private Integer unitsConsumed;

    @NotBlank(message = "Billing month is required")
    private String billingMonth;

    @NotNull(message = "Billing year is required")
    private Integer billingYear;

    private Long meterReadingId; // Optional - link to meter reading
}