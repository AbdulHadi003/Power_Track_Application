package com.powertrack.dto.response.meter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeterResponseDTO {

    private Long id;
    private String meterNumber;
    private String meterType;
    private String connectionAddress;
    private String location;
    private Long feederId;
    private String feederName;
    private String feederCode;
    private String area;
    private String status;
    private LocalDate installationDate;
    private Integer lastReading;
    private String installedByName;
}