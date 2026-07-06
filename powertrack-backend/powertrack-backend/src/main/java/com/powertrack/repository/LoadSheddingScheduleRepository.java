package com.powertrack.repository;

import com.powertrack.entity.LoadSheddingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface LoadSheddingScheduleRepository extends JpaRepository<LoadSheddingSchedule, Long> {

    List<LoadSheddingSchedule> findByFeederId(Long feederId);

    List<LoadSheddingSchedule> findByScheduleDate(LocalDate date);

    List<LoadSheddingSchedule> findByFeederIdAndScheduleDate(Long feederId, LocalDate date);

    List<LoadSheddingSchedule> findByFeederIdAndScheduleDateBetween(
            Long feederId, LocalDate startDate, LocalDate endDate
    );

    @Query("SELECT ls FROM LoadSheddingSchedule ls WHERE ls.feeder.id = ?1 " +
            "AND ls.scheduleDate = CURRENT_DATE ORDER BY ls.timeSlotStart ASC")
    List<LoadSheddingSchedule> findTodaySchedulesForFeeder(Long feederId);

    @Query("SELECT ls FROM LoadSheddingSchedule ls WHERE ls.feeder.id = ?1 " +
            "AND ls.scheduleDate BETWEEN CURRENT_DATE AND ?2 " +
            "ORDER BY ls.scheduleDate, ls.timeSlotStart")
    List<LoadSheddingSchedule> findWeekSchedulesForFeeder(Long feederId, LocalDate weekEndDate);

    @Query("SELECT ls FROM LoadSheddingSchedule ls WHERE ls.feeder.id = ?1 " +
            "AND ls.scheduleDate = ?2 AND ls.timeSlotStart <= ?3 AND ls.timeSlotEnd > ?3")
    List<LoadSheddingSchedule> findActiveSchedule(Long feederId, LocalDate date, LocalTime time);

    @Query("SELECT ls FROM LoadSheddingSchedule ls WHERE ls.scheduleDate >= CURRENT_DATE " +
            "ORDER BY ls.scheduleDate, ls.timeSlotStart")
    List<LoadSheddingSchedule> findUpcomingSchedules();

    List<LoadSheddingSchedule> findByCreatedById(Long userId);

    @Query("SELECT COUNT(ls) FROM LoadSheddingSchedule ls WHERE ls.feeder.id = ?1 " +
            "AND ls.scheduleDate = ?2 AND ls.timeSlotStart = ?3")
    long countExistingSchedule(Long feederId, LocalDate date, LocalTime time);
}
