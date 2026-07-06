package com.powertrack.dto.mapper;

import com.powertrack.dto.response.bill.BillResponseDTO;
import com.powertrack.entity.Bill;
import org.springframework.stereotype.Component;

@Component
public class BillMapper {

    public static BillResponseDTO toDTO(Bill bill) {
        if (bill == null) {
            return null;
        }

        return BillResponseDTO.builder()
                .id(bill.getId())
                .userId(bill.getUserId())
                .userName(bill.getUser() != null ? bill.getUser().getName() : null)
                .meterId(bill.getMeterId())
                .meterNumber(bill.getMeter() != null ? bill.getMeter().getMeterNumber() : null)
                .billingMonth(bill.getBillingMonth())
                .billingYear(bill.getBillingYear())
                .unitsConsumed(bill.getUnitsConsumed())
                .unitRate(bill.getUnitRate())
                .fixedCharges(bill.getFixedCharges())
                .taxAmount(bill.getTaxAmount())
                .penaltyAmount(bill.getPenaltyAmount())
                .totalAmount(bill.getTotalAmount())
                .dueDate(bill.getDueDate())
                .status(bill.getStatus().name())
                .paymentDate(bill.getPaymentDate())
                .paymentMethod(bill.getPaymentMethod())
                .eligibleForInstallment(bill.isEligibleForInstallment())
                .createdAt(bill.getCreatedAt())
                .build();
    }
}