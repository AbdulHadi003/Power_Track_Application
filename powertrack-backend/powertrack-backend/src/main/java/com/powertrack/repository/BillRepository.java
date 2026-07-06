package com.powertrack.repository;

import com.powertrack.entity.Bill;
import com.powertrack.enums.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByUserId(Long userId);

    List<Bill> findByMeterId(Long meterId);

    List<Bill> findByStatus(BillStatus status);

    List<Bill> findByUserIdAndStatus(Long userId, BillStatus status);

    Optional<Bill> findByMeterIdAndBillingMonthAndBillingYear(
            Long meterId, String billingMonth, Integer billingYear
    );

    boolean existsByMeterIdAndBillingMonthAndBillingYear(
            Long meterId, String billingMonth, Integer billingYear
    );

    @Query("SELECT b FROM Bill b WHERE b.status = 'UNPAID' ORDER BY b.dueDate ASC")
    List<Bill> findAllUnpaidBills();

    @Query("SELECT b FROM Bill b WHERE b.status = 'UNPAID' AND b.dueDate < CURRENT_DATE")
    List<Bill> findOverdueBills();

    @Query("SELECT b FROM Bill b WHERE b.user.id = ?1 AND b.status = 'UNPAID' ")
    List<Bill> findUserUnpaidBills(Long userId);

    @Query("SELECT SUM(b.totalAmount) FROM Bill b WHERE b.user.id = ?1 AND b.status = 'UNPAID' ")
    BigDecimal getTotalOutstandingByUserId(Long userId);

    @Query("SELECT SUM(b.totalAmount) FROM Bill b WHERE b.status = 'PAID' " +
            "AND b.billingMonth = ?1 AND b.billingYear = ?2")
    BigDecimal getMonthlyRevenue(String month, Integer year);

    long countByStatus(BillStatus status);

    List<Bill> findByDueDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT b FROM Bill b WHERE b.totalAmount >= 15000 AND b.status = 'UNPAID' ")
    List<Bill> findBillsEligibleForInstallment();
}