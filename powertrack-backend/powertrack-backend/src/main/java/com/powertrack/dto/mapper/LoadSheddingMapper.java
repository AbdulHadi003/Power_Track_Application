package com.powertrack.dto.mapper;

import com.powertrack.dto.request.loadshedding.LoadSheddingScheduleRequest;
import com.powertrack.dto.response.loadshedding.LoadSheddingScheduleDTO;
import com.powertrack.entity.Feeder;
import com.powertrack.entity.LoadSheddingSchedule;
import com.powertrack.entity.User;
import org.springframework.stereotype.Component;

@Component
public class LoadSheddingMapper {

    public static LoadSheddingSchedule toEntity(LoadSheddingScheduleRequest dto, Feeder feeder, User createdBy) {
        LoadSheddingSchedule schedule = new LoadSheddingSchedule();
        schedule.setFeeder(feeder);
        schedule.setScheduleDate(dto.getScheduleDate());
        schedule.setTimeSlotStart(dto.getTimeSlotStart());
        schedule.setTimeSlotEnd(dto.getTimeSlotEnd());
        schedule.setDurationMinutes(dto.getDurationMinutes());
        schedule.setCreatedBy(createdBy);
        return schedule;
    }

    public static LoadSheddingScheduleDTO toDTO(LoadSheddingSchedule schedule) {
        if (schedule == null) {
            return null;
        }

        return LoadSheddingScheduleDTO.builder()
                .id(schedule.getId())
                .feederId(schedule.getFeederId())
                .feederCode(schedule.getFeeder() != null ? schedule.getFeeder().getFeederCode() : null)
                .feederName(schedule.getFeederName())
                .area(schedule.getArea())
                .scheduleDate(schedule.getScheduleDate())
                .timeSlotStart(schedule.getTimeSlotStart())
                .timeSlotEnd(schedule.getTimeSlotEnd())
                .durationMinutes(schedule.getDurationMinutes())
                .isActiveNow(schedule.isActiveNow())
                .createdById(schedule.getCreatedById())
                .build();
    }
}