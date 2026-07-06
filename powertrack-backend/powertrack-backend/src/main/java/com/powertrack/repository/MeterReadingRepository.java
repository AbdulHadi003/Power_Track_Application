package com.powertrack.repository;

import com.powertrack.entity.MeterReading;
import com.powertrack.enums.ReadingStatus;
import com.powertrack.enums.ReadingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {

    List<MeterReading> findByMeterId(Long meterId);

    Optional<MeterReading> findByMeterIdAndReadingMonthAndReadingYear(
            Long meterId, String readingMonth, Integer readingYear
    );

    boolean existsByMeterIdAndReadingMonthAndReadingYear(
            Long meterId, String readingMonth, Integer readingYear
    );

    List<MeterReading> findByFieldStaffId(Long fieldStaffId);

    List<MeterReading> findByStatus(ReadingStatus status);

    @Query("SELECT mr FROM MeterReading mr WHERE mr.status = 'SUBMITTED' ORDER BY mr.readingDate DESC")
    List<MeterReading> findAllSubmittedReadings();

    @Query("SELECT mr FROM MeterReading mr WHERE mr.meter.id = ?1 ORDER BY mr.readingDate DESC")
    Optional<MeterReading> findLatestReadingByMeterId(Long meterId);

    List<MeterReading> findByReadingDateBetween(LocalDate startDate, LocalDate endDate);

    List<MeterReading> findByReadingType(ReadingType readingType);

    @Query("SELECT mr FROM MeterReading mr WHERE mr.meter.id = ?1 ORDER BY mr.readingDate DESC")
    List<MeterReading> findMeterReadingHistory(Long meterId);
}