package com.powertrack.dto.response.installment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstallmentResponseDTO {

    private Long id;
    private Long billId;
    private String billingMonth;
    private Integer billingYear;
    private BigDecimal totalAmount;
    private BigDecimal monthlyAmount;
    private Integer numberOfMonths;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reasonForInstallment;
    private String approvedByName;
    private List<InstallmentPaymentDTO> payments;
}