package com.powertrack.dto.response.meter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeterRequestResponseDTO {

    private Long id;
    private String meterType;
    private String connectionAddress;
    private String feederName;
    private String status;
    private LocalDateTime requestDate;
    private LocalDateTime approvedDate;
    private String approvedByName;
    private String assignedFieldStaffName;
    private String rejectionReason;
}