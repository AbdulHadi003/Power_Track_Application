package com.powertrack.dto.request.complaint;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintRequestDTO {

    @NotBlank(message = "Subject is required")
    @Size(max = 200, message = "Subject cannot exceed 200 characters")
    private String subject;

    @NotBlank(message = "Description is required")
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @NotBlank(message = "Category is required")
    @Pattern(regexp = "COMPLAINT|BILL_INQUIRY|TECHNICAL_ISSUE", message = "Invalid category")
    private String category;

    private Long meterId; // Optional - for meter-specific complaints

    @Size(max = 500, message = "Attachment URL cannot exceed 500 characters")
    private String attachmentUrl; // Optional - for proof/images
}