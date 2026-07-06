package com.powertrack.dto.request.meterreading;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeterReadingRequestDTO {

    @NotNull(message = "Meter ID is required")
    private Long meterId;

    @NotNull(message = "Current reading is required")
    @Min(value = 0, message = "Current reading cannot be negative")
    private Integer currentReading;

    @NotNull(message = "Reading date is required")
    private LocalDate readingDate;

    @NotBlank(message = "Reading month is required")
    private String readingMonth;

    @NotNull(message = "Reading year is required")
    private Integer readingYear;

    @Pattern(regexp = "MONTHLY|EMERGENCY|RECHECK|INSTALLATION", message = "Invalid reading type")
    private String readingType = "MONTHLY";

    @Size(max = 500, message = "Photo URL cannot exceed 500 characters")
    private String photoUrl;

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;
}