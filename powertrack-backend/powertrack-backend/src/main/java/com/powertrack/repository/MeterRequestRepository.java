package com.powertrack.repository;

import com.powertrack.entity.MeterRequest;
import com.powertrack.entity.User;
import com.powertrack.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeterRequestRepository extends JpaRepository<MeterRequest , Long> {

    List<MeterRequest> findByUser(User user);

    List<MeterRequest> findByuser_id(Long userId);

    List<MeterRequest> findByStatus(RequestStatus status);

    @Query("SELECT mr FROM MeterRequest mr WHERE mr.status = 'PENDING' ORDER BY mr.requestDate ASC")
    List<MeterRequest> findAllPendingRequests();

    List<MeterRequest> findByAssignedFieldStaffId(Long fieldStaffId);

    long countByuser_idAndStatus(Long id, RequestStatus status);

    List<MeterRequest> findByFeederId(Long feederId);

    @Query("SELECT COUNT(mr) FROM MeterRequest mr WHERE mr.status = 'PENDING' ")
    long countPendingRequests();
}