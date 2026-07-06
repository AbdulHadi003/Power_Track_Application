package com.powertrack.dto.mapper;

import com.powertrack.dto.response.payment.PaymentResponseDTO;
import com.powertrack.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public static PaymentResponseDTO toDTO(Payment payment) {
        if (payment == null) {
            return null;
        }

        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .billId(payment.getBillId())
                .amountPaid(payment.getAmountPaid())
                .paymentDate(payment.getPaymentDate())
                .paymentMethod(payment.getPaymentMethod().name())
                .transactionId(payment.getTransactionId())
                .status(payment.getStatus().name())
                .receiptNumber(payment.getReceiptNumber())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}