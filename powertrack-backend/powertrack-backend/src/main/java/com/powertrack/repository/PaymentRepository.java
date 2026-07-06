package com.powertrack.repository;

import com.powertrack.entity.Payment;
import com.powertrack.enums.PaymentMethod;
import com.powertrack.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByBillId(Long billId);

    List<Payment> findAllByBillId(Long billId);

    boolean existsByBillIdAndStatus(Long billId, PaymentStatus status);

    Optional<Payment> findByBillIdAndStatus(Long billId, PaymentStatus status);

    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);

    Optional<Payment> findByTransactionId(String transactionId);

    List<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT SUM(p.amountPaid) FROM Payment p WHERE p.status = 'SUCCESS'")
    BigDecimal getTotalRevenue();

    @Query("SELECT SUM(p.amountPaid) FROM Payment p WHERE p.status = 'SUCCESS' " +
            "AND MONTH(p.paymentDate) = ?1 AND YEAR(p.paymentDate) = ?2")
    BigDecimal getMonthlyRevenue(int month, int year);

    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = 'SUCCESS' ")
    long countSuccessfulPayments();

    @Query("SELECT p FROM Payment p WHERE p.status = 'SUCCESS' ORDER BY p.paymentDate DESC")
    List<Payment> findRecentPayments();
}