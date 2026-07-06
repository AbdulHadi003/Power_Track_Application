package com.powertrack.service;

import com.powertrack.dto.mapper.LoadSheddingMapper;
import com.powertrack.dto.request.loadshedding.LoadSheddingScheduleRequest;
import com.powertrack.dto.response.loadshedding.LoadSheddingScheduleDTO;
import com.powertrack.entity.Feeder;
import com.powertrack.entity.LoadSheddingSchedule;
import com.powertrack.entity.User;
import com.powertrack.exception.ResourceNotFoundException;
import com.powertrack.repository.LoadSheddingScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoadSheddingService {

    private final LoadSheddingScheduleRepository scheduleRepository;
    private final FeederService feederService;
    private final UserService userService;

    @Transactional
    public LoadSheddingScheduleDTO createSchedule(LoadSheddingScheduleRequest request, Long adminId) {
        Feeder feeder = feederService.getFeederEntityById(request.getFeederId());
        User admin = userService.getUserEntityById(adminId);

        // Check if schedule already exists for this feeder at this time
        long existingCount = scheduleRepository.countExistingSchedule(
                request.getFeederId(),
                request.getScheduleDate(),
                request.getTimeSlotStart()
        );

        if (existingCount > 0) {
            throw new IllegalStateException("Schedule already exists for this feeder at this time");
        }

        // Create schedule
        LoadSheddingSchedule schedule = new LoadSheddingSchedule();
        schedule.setFeeder(feeder);
        schedule.setScheduleDate(request.getScheduleDate());
        schedule.setTimeSlotStart(request.getTimeSlotStart());
        schedule.setTimeSlotEnd(request.getTimeSlotEnd());
        schedule.setDurationMinutes(request.getDurationMinutes());
        schedule.setCreatedBy(admin);

        LoadSheddingSchedule savedSchedule = scheduleRepository.save(schedule);
        return LoadSheddingMapper.toDTO(savedSchedule);
    }

    @Transactional(readOnly = true)
    public List<LoadSheddingScheduleDTO> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(LoadSheddingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LoadSheddingScheduleDTO> getUpcomingSchedules() {
        return scheduleRepository.findUpcomingSchedules().stream()
                .map(LoadSheddingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LoadSheddingScheduleDTO getScheduleById(Long id) {
        LoadSheddingSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LoadSheddingSchedule", "id", id));
        return LoadSheddingMapper.toDTO(schedule);
    }

    @Transactional(readOnly = true)
    public List<LoadSheddingScheduleDTO> getSchedulesByFeeder(Long feederId) {
        return scheduleRepository.findByFeederId(feederId).stream()
                .map(LoadSheddingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LoadSheddingScheduleDTO> getSchedulesByDate(LocalDate date) {
        return scheduleRepository.findByScheduleDate(date).stream()
                .map(LoadSheddingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LoadSheddingScheduleDTO> getTodaySchedulesForFeeder(Long feederId) {
        return scheduleRepository.findTodaySchedulesForFeeder(feederId).stream()
                .map(LoadSheddingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LoadSheddingScheduleDTO> getWeekSchedulesForFeeder(Long feederId) {
        LocalDate weekEnd = LocalDate.now().plusDays(7);
        return scheduleRepository.findWeekSchedulesForFeeder(feederId, weekEnd).stream()
                .map(LoadSheddingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LoadSheddingScheduleDTO> getMyLoadSheddingSchedule(Long userId) {
        User user = userService.getUserEntityById(userId);

        // Get user's meters and their feeders
        List<Long> feederIds = user.getMeters().stream()
                .map(meter -> meter.getFeeder().getId())
                .distinct()
                .collect(Collectors.toList());

        // Get schedules for all user's feeders
        return feederIds.stream()
                .flatMap(feederId -> scheduleRepository.findByFeederId(feederId).stream())
                .map(LoadSheddingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LoadSheddingScheduleDTO> getTodayLoadSheddingForUser(Long userId) {
        User user = userService.getUserEntityById(userId);

        // Get user's meters and their feeders
        List<Long> feederIds = user.getMeters().stream()
                .map(meter -> meter.getFeeder().getId())
                .distinct()
                .collect(Collectors.toList());

        LocalDate today = LocalDate.now();

        // Get today's schedules for all user's feeders
        return feederIds.stream()
                .flatMap(feederId -> scheduleRepository
                        .findByFeederIdAndScheduleDate(feederId, today).stream())
                .map(LoadSheddingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LoadSheddingScheduleDTO> getActiveScheduleNow(Long feederId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        return scheduleRepository.findActiveSchedule(feederId, today, now).stream()
                .map(LoadSheddingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LoadSheddingScheduleDTO updateSchedule(Long id, LoadSheddingScheduleRequest request) {
        LoadSheddingSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LoadSheddingSchedule", "id", id));

        Feeder feeder = feederService.getFeederEntityById(request.getFeederId());

        schedule.setFeeder(feeder);
        schedule.setScheduleDate(request.getScheduleDate());
        schedule.setTimeSlotStart(request.getTimeSlotStart());
        schedule.setTimeSlotEnd(request.getTimeSlotEnd());
        schedule.setDurationMinutes(request.getDurationMinutes());

        LoadSheddingSchedule updatedSchedule = scheduleRepository.save(schedule);
        return LoadSheddingMapper.toDTO(updatedSchedule);
    }

    @Transactional
    public void deleteSchedule(Long id) {
        LoadSheddingSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LoadSheddingSchedule", "id", id));

        scheduleRepository.delete(schedule);
    }

    @Transactional
    public void deleteOldSchedules(LocalDate cutoffDate) {
        List<LoadSheddingSchedule> oldSchedules = scheduleRepository.findAll().stream()
                .filter(schedule -> schedule.getScheduleDate().isBefore(cutoffDate))
                .collect(Collectors.toList());

        scheduleRepository.deleteAll(oldSchedules);
    }
}