package com.powertrack.dto.request.installment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApproveRejectInstallmentRequest {

    @NotBlank(message = "Action is required (APPROVE or REJECT)")
    @Pattern(regexp = "APPROVE|REJECT", message = "Invalid payment method")
    private String action;

    @Size(max = 500, message = "Reason cannot exceed 500 characters")
    private String rejectionReason; // Required if REJECT
}