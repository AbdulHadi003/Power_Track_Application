package com.powertrack.dto.response.feeder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeederResponseDTO {

    private Long id;
    private String feederCode;
    private String feederName;
    private String area;
    private String substation;
    private Integer capacityKw;
    private String status;
    private LocalDateTime createdAt;
}