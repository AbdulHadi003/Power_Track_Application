package com.powertrack.dto.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionAnalyticsDTO {

    private Long meterId;
    private String meterNumber;
    private List<MonthlyConsumptionDTO> monthlyData;
    private Double averageMonthlyConsumption;
    private Integer highestConsumption;
    private Integer lowestConsumption;
    private String trend; // INCREASING, DECREASING, STABLE
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class MonthlyConsumptionDTO {
    private String month;
    private Integer year;
    private Integer unitsConsumed;
    private BigDecimal totalAmount;
}