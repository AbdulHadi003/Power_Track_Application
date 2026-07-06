package com.powertrack.controller;

import com.powertrack.dto.request.loadshedding.LoadSheddingScheduleRequest;
import com.powertrack.dto.response.loadshedding.LoadSheddingScheduleDTO;
import com.powertrack.service.LoadSheddingService;
import com.powertrack.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/load-shedding")
@RequiredArgsConstructor
public class LoadSheddingController {

    private final LoadSheddingService loadSheddingService;
    private final SecurityUtils securityUtils;

    @GetMapping("/my-schedule")
    public ResponseEntity<List<LoadSheddingScheduleDTO>> getMyLoadSheddingSchedule() {
        Long userId = securityUtils.getCurrentUserId();
        List<LoadSheddingScheduleDTO> schedules = loadSheddingService.getMyLoadSheddingSchedule(userId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/my-schedule/today")
    public ResponseEntity<List<LoadSheddingScheduleDTO>> getTodayLoadSheddingForUser() {
        Long userId = securityUtils.getCurrentUserId();
        List<LoadSheddingScheduleDTO> schedules = loadSheddingService.getTodayLoadSheddingForUser(userId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoadSheddingScheduleDTO> getScheduleById(@PathVariable Long id) {
        LoadSheddingScheduleDTO schedule = loadSheddingService.getScheduleById(id);
        return ResponseEntity.ok(schedule);
    }

    @GetMapping("/feeder/{feederId}")
    public ResponseEntity<List<LoadSheddingScheduleDTO>> getSchedulesByFeeder(@PathVariable Long feederId) {
        List<LoadSheddingScheduleDTO> schedules = loadSheddingService.getSchedulesByFeeder(feederId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/feeder/{feederId}/today")
    public ResponseEntity<List<LoadSheddingScheduleDTO>> getTodaySchedulesForFeeder(@PathVariable Long feederId) {
        List<LoadSheddingScheduleDTO> schedules = loadSheddingService.getTodaySchedulesForFeeder(feederId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/feeder/{feederId}/week")
    public ResponseEntity<List<LoadSheddingScheduleDTO>> getWeekSchedulesForFeeder(@PathVariable Long feederId) {
        List<LoadSheddingScheduleDTO> schedules = loadSheddingService.getWeekSchedulesForFeeder(feederId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<LoadSheddingScheduleDTO>> getSchedulesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<LoadSheddingScheduleDTO> schedules = loadSheddingService.getSchedulesByDate(date);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/feeder/{feederId}/active")
    public ResponseEntity<List<LoadSheddingScheduleDTO>> getActiveScheduleNow(@PathVariable Long feederId) {
        List<LoadSheddingScheduleDTO> schedules = loadSheddingService.getActiveScheduleNow(feederId);
        return ResponseEntity.ok(schedules);
    }

    // Admin endpoints

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LoadSheddingScheduleDTO>> getAllSchedules() {
        List<LoadSheddingScheduleDTO> schedules = loadSheddingService.getAllSchedules();
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/upcoming")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LoadSheddingScheduleDTO>> getUpcomingSchedules() {
        List<LoadSheddingScheduleDTO> schedules = loadSheddingService.getUpcomingSchedules();
        return ResponseEntity.ok(schedules);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoadSheddingScheduleDTO> createSchedule(
            @Valid @RequestBody LoadSheddingScheduleRequest request) {
        Long adminId = securityUtils.getCurrentUserId();
        LoadSheddingScheduleDTO schedule = loadSheddingService.createSchedule(request, adminId);
        return ResponseEntity.status(HttpStatus.CREATED).body(schedule);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoadSheddingScheduleDTO> updateSchedule(
            @PathVariable Long id,
            @Valid @RequestBody LoadSheddingScheduleRequest request) {
        LoadSheddingScheduleDTO schedule = loadSheddingService.updateSchedule(id, request);
        return ResponseEntity.ok(schedule);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteSchedule(@PathVariable Long id) {
        loadSheddingService.deleteSchedule(id);
        return ResponseEntity.ok("Load shedding schedule deleted successfully");
    }

    @DeleteMapping("/cleanup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteOldSchedules(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate cutoffDate) {
        loadSheddingService.deleteOldSchedules(cutoffDate);
        return ResponseEntity.ok("Old schedules deleted successfully");
    }
}