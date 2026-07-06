package com.powertrack.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.powertrack.enums.MeterType;
import com.powertrack.enums.RequestStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "meter_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeterRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @NotNull(message = "Meter type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "meter_type_requested", nullable = false, length = 50)
    private MeterType meterTypeRequested;

    @NotBlank(message = "Connection address is required")
    @Column(name = "connection_address", nullable = false, columnDefinition = "TEXT")
    private String connectionAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feeder_id", nullable = false)
    @JsonIgnore
    private Feeder feeder;

    @Column(name = "reason_for_request", length = 500)
    private String reasonForRequest;

    @CreationTimestamp
    @Column(name = "request_date", nullable = false, updatable = false)
    private LocalDateTime requestDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RequestStatus status = RequestStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    @JsonIgnore
    private User approvedBy;

    @Column(name = "approved_date")
    private LocalDateTime approvedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_field_staff_id")
    @JsonIgnore
    private User assignedFieldStaff;

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public Long getFeederId() {
        return feeder != null ? feeder.getId() : null;
    }

    public Long getApprovedById() {
        return approvedBy != null ? approvedBy.getId() : null;
    }

    public Long getAssignedFieldStaffId() {
        return assignedFieldStaff != null ? assignedFieldStaff.getId() : null;
    }
}