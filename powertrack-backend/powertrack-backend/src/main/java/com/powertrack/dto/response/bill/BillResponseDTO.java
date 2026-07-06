package com.powertrack.dto.response.bill;

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
public class BillResponseDTO {

    private Long id;
    private Long userId;
    private String userName;
    private Long meterId;
    private String meterNumber;
    private String billingMonth;
    private Integer billingYear;
    private Integer unitsConsumed;
    private BigDecimal unitRate;
    private BigDecimal fixedCharges;
    private BigDecimal taxAmount;
    private BigDecimal penaltyAmount;
    private BigDecimal totalAmount;
    private LocalDate dueDate;
    private String status;
    private LocalDate paymentDate;
    private String paymentMethod;
    private Boolean eligibleForInstallment;
    private LocalDateTime createdAt;
}