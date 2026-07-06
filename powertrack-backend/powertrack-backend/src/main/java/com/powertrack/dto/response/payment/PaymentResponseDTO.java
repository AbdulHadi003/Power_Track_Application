package com.powertrack.dto.response.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {

    private Long id;
    private Long billId;
    private BigDecimal amountPaid;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private String transactionId;
    private String status;
    private String receiptNumber;
    private LocalDateTime createdAt;
}