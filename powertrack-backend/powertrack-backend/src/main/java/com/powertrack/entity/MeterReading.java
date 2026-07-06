package com.powertrack.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.powertrack.enums.ReadingStatus;
import com.powertrack.enums.ReadingType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "meter_readings",
        uniqueConstraints = @UniqueConstraint(columnNames = {"meter_id", "reading_month", "reading_year"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeterReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meter_id", nullable = false)
    @JsonIgnore
    private Meter meter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_staff_id", nullable = false)
    @JsonIgnore
    private User fieldStaff;

    @NotNull(message = "Previous reading is required")
    @Column(name = "previous_reading", nullable = false)
    private Integer previousReading;

    @NotNull(message = "Current reading is required")
    @Column(name = "current_reading", nullable = false)
    private Integer currentReading;

    @NotNull(message = "Units consumed is required")
    @Column(name = "units_consumed", nullable = false)
    private Integer unitsConsumed;

    @NotNull(message = "Reading date is required")
    @Column(name = "reading_date", nullable = false)
    private LocalDate readingDate;

    @Column(name = "reading_month", nullable = false, length = 20)
    private String readingMonth;

    @Column(name = "reading_year", nullable = false)
    private Integer readingYear;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "reading_type", nullable = false, length = 20)
    private ReadingType readingType = ReadingType.MONTHLY;

    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReadingStatus status = ReadingStatus.SUBMITTED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by")
    @JsonIgnore
    private User verifiedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Long getMeterId() {
        return meter != null ? meter.getId() : null;
    }

    public Long getFieldStaffId() {
        return fieldStaff != null ? fieldStaff.getId() : null;
    }

    public Long getVerifiedById() {
        return verifiedBy != null ? verifiedBy.getId() : null;
    }
}