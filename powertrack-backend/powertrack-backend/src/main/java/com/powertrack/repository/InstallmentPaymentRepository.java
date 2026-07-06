package com.powertrack.repository;

import com.powertrack.entity.InstallmentPayment;
import com.powertrack.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InstallmentPaymentRepository extends JpaRepository<InstallmentPayment, Long> {

    List<InstallmentPayment> findByInstallmentRequestId(Long installmentRequestId);

    List<InstallmentPayment> findByStatus(PaymentStatus status);

    List<InstallmentPayment> findByInstallmentRequestIdAndStatus(Long installmentRequestId, PaymentStatus status);

    Optional<InstallmentPayment> findByInstallmentRequestIdAndInstallmentNumber(
            Long installmentRequestId, Integer installmentNumber
    );

    @Query("SELECT ip FROM InstallmentPayment ip WHERE ip.status = 'PENDING' AND ip.dueDate < CURRENT_DATE")
    List<InstallmentPayment> findOverduePayments();

    List<InstallmentPayment> findByDueDateBetween(LocalDate startDate, LocalDate endDate);

    long countByInstallmentRequestIdAndStatus(Long installmentRequestId, PaymentStatus status);

    @Query("SELECT COUNT(ip) FROM InstallmentPayment ip WHERE ip.installmentRequest.id = ?1 AND ip.status = 'PAID' ")
    long countPaidInstallments(Long installmentRequestId);

    @Query("SELECT ip FROM InstallmentPayment ip WHERE ip.installmentRequest.id = ?1 " +
            "AND ip.status = 'PENDING' ORDER BY ip.dueDate ASC")
    Optional<InstallmentPayment> findNextDuePayment(Long installmentRequestId);
}