package com.powertrack.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.powertrack.enums.FeederStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "feeders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feeder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Feeder code is required")
    @Column(name = "feeder_code", nullable = false, unique = true, length = 50)
    private String feederCode;

    @NotBlank(message = "Feeder name is required")
    @Column(name = "feeder_name", nullable = false, length = 150)
    private String feederName;

    @NotBlank(message = "Area is required")
    @Column(nullable = false, length = 200)
    private String area;

    @Column(length = 150)
    private String substation;

    @Column(name = "capacity_kw")
    private Integer capacityKw;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FeederStatus status = FeederStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ========== RELATIONSHIPS - REMOVED CASCADE ==========

    @OneToMany(mappedBy = "feeder")
    @JsonIgnore
    private List<Meter> meters;

    @OneToMany(mappedBy = "feeder")
    @JsonIgnore
    private List<MeterRequest> meterRequests;

    @OneToMany(mappedBy = "feeder")
    @JsonIgnore
    private List<LoadSheddingSchedule> loadSheddingSchedules;
}