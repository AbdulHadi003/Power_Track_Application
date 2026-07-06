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
public class UpdateComplaintRequest {

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "IN_PROGRESS|FORWARDED|RESOLVED|CLOSED", message = "Invalid status")
    private String status;

    @Size(max = 2000, message = "Resolution notes cannot exceed 2000 characters")
    private String resolutionNotes;

    @Pattern(regexp = "LOW|MEDIUM|HIGH", message = "Invalid priority")
    private String priority; // Optional - CSR/Admin can update priority
}