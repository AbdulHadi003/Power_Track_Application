package com.powertrack.dto.request.meter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApproveRejectMeterRequest {

    @NotBlank(message = "Action is required")
    @Pattern(regexp = "APPROVE|REJECT", message = "Action must be either APPROVE or REJECT")
    private String action;

    private Long assignedFieldStaffId; // Required if APPROVE

    @Size(max = 500, message = "Rejection reason cannot exceed 500 characters")
    private String rejectionReason; // Required if REJECT
}