package com.powertrack.dto.mapper;

import com.powertrack.dto.response.installment.InstallmentPaymentDTO;
import com.powertrack.dto.response.installment.InstallmentResponseDTO;
import com.powertrack.entity.InstallmentPayment;
import com.powertrack.entity.InstallmentRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InstallmentRequestMapper {

    public static InstallmentResponseDTO toDTO(InstallmentRequest request) {
        if (request == null) {
            return null;
        }

        List<InstallmentPaymentDTO> paymentDTOs = request.getInstallmentPayments() != null ?
                request.getInstallmentPayments().stream()
                        .map(InstallmentRequestMapper::toPaymentDTO)
                        .collect(Collectors.toList()) : null;

        return InstallmentResponseDTO.builder()
                .id(request.getId())
                .billId(request.getBill() != null ? request.getBill().getId() : null)
                .billingMonth(request.getBill() != null ? request.getBill().getBillingMonth() : null)
                .billingYear(request.getBill() != null ? request.getBill().getBillingYear() : null)
                .totalAmount(request.getTotalAmount())
                .monthlyAmount(request.getMonthlyInstallment())
                .numberOfMonths(request.getNumberOfMonths())
                .status(request.getStatus().name())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reasonForInstallment(request.getReasonForInstallment())
                .approvedByName(request.getApprovedBy() != null ? request.getApprovedBy().getName() : null)
                .payments(paymentDTOs)
                .build();
    }

    public static InstallmentPaymentDTO toPaymentDTO(InstallmentPayment payment) {
        if (payment == null) {
            return null;
        }

        return InstallmentPaymentDTO.builder()
                .id(payment.getId())
                .installmentNumber(payment.getInstallmentNumber())
                .amount(payment.getAmount())
                .dueDate(payment.getDueDate())
                .paymentDate(payment.getPaymentDate())
                .status(payment.getStatus().name())
                .paymentMethod(payment.getPaymentMethod() != null ? payment.getPaymentMethod().name() : null)
                .receiptNumber(payment.getReceiptNumber())
                .build();
    }
}