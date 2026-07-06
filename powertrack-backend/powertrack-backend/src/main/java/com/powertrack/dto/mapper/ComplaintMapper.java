package com.powertrack.dto.mapper;

import com.powertrack.dto.request.complaint.ComplaintRequestDTO;
import com.powertrack.dto.response.complaint.ComplaintResponseDTO;
import com.powertrack.entity.Complaint;
import com.powertrack.entity.Meter;
import com.powertrack.entity.User;
import com.powertrack.enums.ComplaintCategory;
import com.powertrack.enums.ComplaintPriority;
import com.powertrack.enums.ComplaintStatus;
import org.springframework.stereotype.Component;

@Component
public class ComplaintMapper {

    public static Complaint toEntity(ComplaintRequestDTO dto, User user, Meter meter) {
        Complaint complaint = new Complaint();
        complaint.setUser(user);
        complaint.setMeter(meter);
        complaint.setSubject(dto.getSubject());
        complaint.setDescription(dto.getDescription());
        complaint.setCategory(ComplaintCategory.valueOf(dto.getCategory()));
        complaint.setStatus(ComplaintStatus.NEW);
        complaint.setPriority(ComplaintPriority.MEDIUM);
        complaint.setAttachmentUrl(dto.getAttachmentUrl());
        return complaint;
    }

    public static ComplaintResponseDTO toDTO(Complaint complaint) {
        if (complaint == null) {
            return null;
        }

        return ComplaintResponseDTO.builder()
                .id(complaint.getId())
                .complaintToken(complaint.getComplaintToken())
                .userName(complaint.getUser() != null ? complaint.getUser().getName() : null)
                .meterNumber(complaint.getMeter() != null ? complaint.getMeter().getMeterNumber() : null)
                .subject(complaint.getSubject())
                .description(complaint.getDescription())
                .category(complaint.getCategory().name())
                .status(complaint.getStatus().name())
                .priority(complaint.getPriority().name())
                .attachmentUrl(complaint.getAttachmentUrl())
                .slaDueDate(complaint.getSlaDueDate())
                .handledByName(complaint.getHandledBy() != null ? complaint.getHandledBy().getName() : null)
                .forwardedToName(complaint.getForwardedTo() != null ? complaint.getForwardedTo().getName() : null)
                .resolutionNotes(complaint.getResolutionNotes())
                .createdAt(complaint.getCreatedAt())
                .resolvedAt(complaint.getResolvedAt())
                .build();
    }
}