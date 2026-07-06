package com.powertrack.dto.response.complaint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintResponseDTO {

    private Long id;
    private String complaintToken;
    private String userName;
    private String meterNumber;
    private String subject;
    private String description;
    private String category;
    private String status;
    private String priority;
    private String attachmentUrl;
    private LocalDateTime slaDueDate;
    private String handledByName;
    private String forwardedToName;
    private String resolutionNotes;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
}
