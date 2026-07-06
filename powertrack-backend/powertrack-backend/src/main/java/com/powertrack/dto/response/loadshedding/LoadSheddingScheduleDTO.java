package com.powertrack.dto.response.loadshedding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoadSheddingScheduleDTO {

    private Long id;
    private Long feederId;
    private String feederCode;
    private String feederName;
    private String area;
    private LocalDate scheduleDate;
    private LocalTime timeSlotStart;
    private LocalTime timeSlotEnd;
    private Integer durationMinutes;
    private Boolean isActiveNow;
    private Long createdById;
    private String createdByName;
    private LocalDateTime createdAt;
}