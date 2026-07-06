package com.powertrack.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.powertrack.enums.ComplaintCategory;
import com.powertrack.enums.ComplaintPriority;
import com.powertrack.enums.ComplaintStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "complaints")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meter_id")
    @JsonIgnore
    private Meter meter;

    @Column(name = "complaint_token", nullable = false, unique = true, length = 20)
    private String complaintToken;

    @NotBlank(message = "Subject is required")
    @Column(nullable = false, length = 200)
    private String subject;

    @NotBlank(message = "Description is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Category is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ComplaintCategory category;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ComplaintStatus status = ComplaintStatus.NEW;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ComplaintPriority priority = ComplaintPriority.MEDIUM;

    @Column(name = "attachment_url", length = 500)
    private String attachmentUrl;

    @Column(name = "sla_due_date")
    private LocalDateTime slaDueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handled_by")
    @JsonIgnore
    private User handledBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forwarded_to")
    @JsonIgnore
    private User forwardedTo;

    @Column(name = "resolution_notes", columnDefinition = "TEXT")
    private String resolutionNotes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public Long getMeterId() {
        return meter != null ? meter.getId() : null;
    }

    public Long getHandledById() {
        return handledBy != null ? handledBy.getId() : null;
    }

    public Long getForwardedToId() {
        return forwardedTo != null ? forwardedTo.getId() : null;
    }

    @PrePersist
    public void generateComplaintToken() {
        if (this.complaintToken == null) {
            this.complaintToken = "CPT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }

        if (this.slaDueDate == null) {
            this.slaDueDate = LocalDateTime.now().plusHours(48);
        }
    }
}