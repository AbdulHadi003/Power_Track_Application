package com.powertrack.repository;

import com.powertrack.entity.Feeder;
import com.powertrack.enums.FeederStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeederRepository extends JpaRepository<Feeder, Long> {

    Optional<Feeder> findByFeederCode(String feederCode);

    boolean existsByFeederCode(String feederCode);

    List<Feeder> findByAreaContainingIgnoreCase(String area);

    List<Feeder> findByStatus(FeederStatus status);

    @Query("SELECT f FROM Feeder f WHERE f.status = 'ACTIVE'")
    List<Feeder> findAllActiveFeeders();

    List<Feeder> findBySubstation(String substation);
}
