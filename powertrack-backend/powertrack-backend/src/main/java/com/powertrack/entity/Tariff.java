package com.powertrack.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tariffs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tariff name is required")
    @Column(name = "tariff_name", nullable = false, length = 100)
    private String tariffName;

    @NotNull(message = "Unit price is required")
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @NotNull(message = "Minimum units is required")
    @Column(name = "min_units", nullable = false)
    private Integer minUnits;

    @Column(name = "max_units")
    private Integer maxUnits;

    @NotNull(message = "Fixed charge is required")
    @Column(name = "fixed_charge", nullable = false, precision = 10, scale = 2)
    private BigDecimal fixedCharge;

    @NotNull(message = "Tax percentage is required")
    @Column(name = "tax_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal taxPercentage;

    @NotNull(message = "Penalty percentage is required")
    @Column(name = "penalty_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal penaltyPercentage;

    @NotNull(message = "Effective from date is required")
    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @JsonIgnore
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Long getCreatedById() {
        return createdBy != null ? createdBy.getId() : null;
    }
}