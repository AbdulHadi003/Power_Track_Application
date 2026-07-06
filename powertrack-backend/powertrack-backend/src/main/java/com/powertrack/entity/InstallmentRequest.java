package com.powertrack.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.powertrack.enums.RequestStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "installment_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstallmentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false, unique = true)
    @JsonIgnore
    private Bill bill;

    @NotNull(message = "Total amount is required")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @NotNull(message = "Monthly installment is required")
    @Column(name = "monthly_installment", nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyInstallment;

    @NotNull(message = "Number of months is required")
    @Min(value = 2, message = "Minimum 2 months require")
    @Max(value = 6, message = "Maximum 6 months allowed")
    @Column(name = "number_of_months", nullable = false)
    private Integer numberOfMonths;
    @Column(name = "reason_for_installment", length = 500)
    private String reasonForInstallment;

    @CreationTimestamp
    @Column(name = "requested_date", nullable = false, updatable = false)
    private LocalDateTime requestedDate;

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

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

// ========== RELATIONSHIPS - KEEP CASCADE ==========

    @OneToMany(mappedBy = "installmentRequest", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<InstallmentPayment> installmentPayments;

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public Long getBillId() {
        return bill != null ? bill.getId() : null;
    }

    public Long getApprovedById() {
        return approvedBy != null ? approvedBy.getId() : null;
    }

    public void calculateDates() {
        if (this.startDate == null) {
            this.startDate = LocalDate.now().plusMonths(1);
        }
        if (this.endDate == null && this.numberOfMonths != null) {
            this.endDate = this.startDate.plusMonths(this.numberOfMonths - 1);
        }
    }
}
