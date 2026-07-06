package com.powertrack.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.powertrack.enums.MeterStatus;
import com.powertrack.enums.MeterType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "meters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feeder_id", nullable = false)
    @JsonIgnore
    private Feeder feeder;

    @NotBlank(message = "Meter number is required")
    @Column(name = "meter_number", nullable = false, unique = true, length = 20)
    private String meterNumber;

    @NotNull(message = "Meter type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "meter_type", nullable = false, length = 50)
    private MeterType meterType;

    @NotBlank(message = "Connection address is required")
    @Column(name = "connection_address", nullable = false, columnDefinition = "TEXT")
    private String connectionAddress;

    @Column(length = 255)
    private String location;

    @Column(name = "installation_date")
    private LocalDate installationDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MeterStatus status = MeterStatus.PENDING;

    @Column(name = "last_reading")
    private Integer lastReading = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "installed_by")
    @JsonIgnore
    private User installedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "installation_verified_by")
    @JsonIgnore
    private User installationVerifiedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ========== RELATIONSHIPS - REMOVED CASCADE ==========

    @OneToMany(mappedBy = "meter")
    @JsonIgnore
    private List<Bill> bills;

    @OneToMany(mappedBy = "meter")
    @JsonIgnore
    private List<MeterReading> meterReadings;

    @OneToMany(mappedBy = "meter")
    @JsonIgnore
    private List<Complaint> complaints;

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public Long getFeederId() {
        return feeder != null ? feeder.getId() : null;
    }
}