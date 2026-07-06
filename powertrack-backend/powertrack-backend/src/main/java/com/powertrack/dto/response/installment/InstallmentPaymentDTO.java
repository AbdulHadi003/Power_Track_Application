package com.powertrack.dto.response.installment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstallmentPaymentDTO {

    private Long id;
    private Integer installmentNumber;
    private BigDecimal amount;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private String status;
    private String paymentMethod;
    private String receiptNumber;
}