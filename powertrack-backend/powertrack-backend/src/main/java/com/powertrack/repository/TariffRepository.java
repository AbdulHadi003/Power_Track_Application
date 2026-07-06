package com.powertrack.repository;

import com.powertrack.entity.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {

    List<Tariff> findByIsActiveTrue();

    @Query("SELECT t FROM Tariff t WHERE t.isActive = true " +
            "AND t.effectiveFrom <= CURRENT_DATE " +
            "AND (t.effectiveTo IS NULL OR t.effectiveTo >= CURRENT_DATE) " +
            "ORDER BY t.effectiveFrom DESC")
    Optional<Tariff> findCurrentActiveTariff();

    Optional<Tariff> findByTariffName(String tariffName);

    List<Tariff> findByEffectiveFromBetween(LocalDate startDate, LocalDate endDate);

    List<Tariff> findByCreatedById(Long userId);

    boolean existsByTariffName(String tariffName);
}