package com.powertrack.dto.request.meter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeterRequestDTO {

    @NotBlank(message = "Meter type is required")
    @Pattern(regexp = "RESIDENTIAL|COMMERCIAL|INDUSTRIAL", message = "Invalid meter type")
    private String meterType;

    @NotBlank(message = "Connection address is required")
    @Size(max = 1000, message = "Connection address cannot exceed 1000 characters")
    private String connectionAddress;

    @NotNull(message = "Feeder ID is required")
    private Long feederId;

    @Size(max = 500, message = "Reason cannot exceed 500 characters")
    private String reasonForRequest;
}