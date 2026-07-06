package com.powertrack.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "load_shedding_schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoadSheddingSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feeder_id", nullable = false)
    @JsonIgnore
    private Feeder feeder;

    @NotNull(message = "Schedule date is required")
    @Column(name = "schedule_date", nullable = false)
    private LocalDate scheduleDate;

    @NotNull(message = "Time slot start is required")
    @Column(name = "time_slot_start", nullable = false)
    private LocalTime timeSlotStart;

    @NotNull(message = "Time slot end is required")
    @Column(name = "time_slot_end", nullable = false)
    private LocalTime timeSlotEnd;

    @NotNull(message = "Duration is required")
    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @JsonIgnore
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Long getFeederId() {
        return feeder != null ? feeder.getId() : null;
    }

    public String getFeederName() {
        return feeder != null ? feeder.getFeederName() : null;
    }

    public String getArea() {
        return feeder != null ? feeder.getArea() : null;
    }

    public Long getCreatedById() {
        return createdBy != null ? createdBy.getId() : null;
    }

    public boolean isActiveNow() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        return scheduleDate.equals(today)
                && now.isAfter(timeSlotStart)
                && now.isBefore(timeSlotEnd);
    }
}