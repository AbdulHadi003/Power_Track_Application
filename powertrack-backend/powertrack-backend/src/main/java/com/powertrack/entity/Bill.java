package com.powertrack.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.powertrack.enums.BillStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meter_id", nullable = false)
    @JsonIgnore
    private Meter meter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meter_reading_id")
    @JsonIgnore
    private MeterReading meterReading;

    @NotBlank(message = "Billing month is required")
    @Column(name = "billing_month", nullable = false, length = 20)
    private String billingMonth;

    @NotNull(message = "Billing year is required")
    @Column(name = "billing_year", nullable = false)
    private Integer billingYear;

    @NotNull(message = "Units consumed is required")
    @Column(name = "units_consumed", nullable = false)
    private Integer unitsConsumed;

    @NotNull(message = "Unit rate is required")
    @Column(name = "unit_rate", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitRate;

    @NotNull(message = "Fixed charges is required")
    @Column(name = "fixed_charges", nullable = false, precision = 10, scale = 2)
    private BigDecimal fixedCharges;

    @NotNull(message = "Tax amount is required")
    @Column(name = "tax_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "penalty_amount", precision = 10, scale = 2)
    private BigDecimal penaltyAmount = BigDecimal.ZERO;

    @NotNull(message = "Total amount is required")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @NotNull(message = "Due date is required")
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BillStatus status = BillStatus.UNPAID;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ========== RELATIONSHIPS - KEEP CASCADE ==========

    @OneToOne(mappedBy = "bill", cascade = CascadeType.ALL)
    @JsonIgnore
    private Payment payment;

    @OneToOne(mappedBy = "bill", cascade = CascadeType.ALL)
    @JsonIgnore
    private InstallmentRequest installmentRequest;

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public Long getMeterId() {
        return meter != null ? meter.getId() : null;
    }

    public Long getMeterReadingId() {
        return meterReading != null ? meterReading.getId() : null;
    }

    public boolean isEligibleForInstallment() {
        return totalAmount.compareTo(new BigDecimal("15000")) >= 0
                && status == BillStatus.UNPAID
                && installmentRequest == null;
    }
}