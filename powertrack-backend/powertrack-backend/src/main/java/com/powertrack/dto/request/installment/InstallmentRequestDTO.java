package com.powertrack.dto.request.installment;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstallmentRequestDTO {

    @NotNull(message = "Bill ID is required")
    private Long billId;

    @NotNull(message = "Number of months is required")
    @Min(value = 2, message = "Minimum 2 months required")
    @Max(value = 6, message = "Maximum 6 months allowed")
    private Integer numberOfMonths;

    @Size(max = 500, message = "Reason cannot exceed 500 characters")
    private String reasonForInstallment;
}
