package com.powertrack.dto.request.loadshedding;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoadSheddingScheduleRequest {

    @NotNull(message = "Feeder ID is required")
    private Long feederId;

    @NotNull(message = "Schedule date is required")
    private LocalDate scheduleDate;

    @NotNull(message = "Time slot start is required")
    private LocalTime timeSlotStart;

    @NotNull(message = "Time slot end is required")
    private LocalTime timeSlotEnd;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationMinutes;
}