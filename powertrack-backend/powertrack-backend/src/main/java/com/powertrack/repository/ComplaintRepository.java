package com.powertrack.repository;

import com.powertrack.entity.Complaint;
import com.powertrack.enums.ComplaintCategory;
import com.powertrack.enums.ComplaintPriority;
import com.powertrack.enums.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    Optional<Complaint> findByComplaintToken(String complaintToken);

    List<Complaint> findByUserId(Long userId);

    List<Complaint> findByMeterId(Long meterId);

    List<Complaint> findByStatus(ComplaintStatus status);

    List<Complaint> findByCategory(ComplaintCategory category);

    List<Complaint> findByPriority(ComplaintPriority priority);

    @Query("SELECT c FROM Complaint c WHERE c.status = 'NEW' ORDER BY c.createdAt DESC")
    List<Complaint> findNewComplaints();

    List<Complaint> findByHandledById(Long csrId);

    List<Complaint> findByForwardedToId(Long adminId);

    @Query("SELECT c FROM Complaint c WHERE c.user.id = ?1 " +
            "AND c.status NOT IN ('RESOLVED', 'CLOSED')")
    List<Complaint> findUserOpenComplaints(Long userId);

    @Query("SELECT c FROM Complaint c WHERE c.slaDueDate < ?1 " +
            "AND c.status NOT IN ('RESOLVED', 'CLOSED')")
    List<Complaint> findOverdueComplaints(LocalDateTime now);

    long countByStatus(ComplaintStatus status);

    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.status IN " +
            "('NEW' , 'IN_PROGRESS','FORWARDED')")
    long countPendingComplaints();

    List<Complaint> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT c FROM Complaint c WHERE c.subject LIKE %?1% OR c.description LIKE %?1%")
    List<Complaint> searchComplaints(String keyword);

    @Query("SELECT c FROM Complaint c WHERE c.status IN " +
            "('NEW', 'IN_PROGRESS', 'FORWARDED') " +
            "ORDER BY c.createdAt DESC")
    List<Complaint> findPendingComplaints();

    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.status = 'NEW'")
    long countNewComplaints();

}