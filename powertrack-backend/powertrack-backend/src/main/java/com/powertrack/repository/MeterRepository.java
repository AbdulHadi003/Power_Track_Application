package com.powertrack.repository;

import com.powertrack.entity.Meter;
import com.powertrack.entity.User;
import com.powertrack.enums.MeterStatus;
import com.powertrack.enums.MeterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeterRepository extends JpaRepository<Meter, Long> {

    Optional<Meter> findByMeterNumber(String meterNumber);

    boolean existsByMeterNumber(String meterNumber);

    List<Meter> findByUser(User user);

    List<Meter> findByUserId(Long userId);

    List<Meter> findByStatus(MeterStatus status);

    List<Meter> findByUserIdAndStatus(Long userId, MeterStatus status);

    long countByUserIdAndStatus(Long userId, MeterStatus status);

    List<Meter> findByFeederId(Long feederId);

    List<Meter> findByMeterType(MeterType meterType);

    List<Meter> findByInstalledById(Long fieldStaffId);

    @Query("SELECT m FROM Meter m WHERE m.status = 'ACTIVE'")
    List<Meter> findAllActiveMeters();

    @Query("SELECT COUNT(m) FROM Meter m WHERE m.status = 'ACTIVE' ")
    long countActiveMeters();
}
