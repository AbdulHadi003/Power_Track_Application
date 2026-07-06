package com.powertrack.repository;

import com.powertrack.entity.InstallmentRequest;
import com.powertrack.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstallmentRequestRepository extends JpaRepository<InstallmentRequest, Long> {

    List<InstallmentRequest> findByUserId(Long userId);

    Optional<InstallmentRequest> findByBillId(Long billId);

    boolean existsByBillId(Long billId);

    List<InstallmentRequest> findByStatus(RequestStatus status);

    @Query("SELECT ir FROM InstallmentRequest ir WHERE ir.status ='PENDING' ORDER BY ir.requestedDate ASC")
    List<InstallmentRequest> findAllPendingRequests();

    @Query("SELECT ir FROM InstallmentRequest ir WHERE ir.status ='APPROVED'")
    List<InstallmentRequest> findAllApprovedRequests();

    @Query("SELECT ir FROM InstallmentRequest ir WHERE ir.user.id = ?1 " +
            "AND (ir.status = 'APPROVED' OR ir.status = 'PENDING')")
    List<InstallmentRequest> findUserActiveInstallments(Long userId);

    @Query("SELECT COUNT(ir) FROM InstallmentRequest ir WHERE ir.user.id = ?1 " +
            "AND (ir.status = 'APPROVED' OR ir.status = 'PENDING')")
    long countUserActiveInstallments(Long userId);

    @Query("SELECT COUNT(ir) FROM InstallmentRequest ir WHERE ir.status = 'PENDING'")
    long countPendingRequests();
}