package com.powertrack.dto.request.installment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayInstallmentRequest {

    @NotNull(message = "Installment payment ID is required")
    private Long installmentPaymentId;

    @NotBlank(message = "Payment method is required")
    @Pattern(regexp = "CASH|CARD|ONLINE|BANK_TRANSFER", message = "Invalid payment method")
    private String paymentMethod;

    private String transactionId; // Optional - for online payments
}
