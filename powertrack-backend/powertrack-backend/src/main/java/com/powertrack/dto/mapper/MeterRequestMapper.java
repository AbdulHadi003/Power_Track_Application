package com.powertrack.dto.mapper;

import com.powertrack.dto.request.meter.MeterRequestDTO;
import com.powertrack.dto.response.meter.MeterRequestResponseDTO;
import com.powertrack.entity.Feeder;
import com.powertrack.entity.MeterRequest;
import com.powertrack.entity.User;
import com.powertrack.enums.MeterType;
import com.powertrack.enums.RequestStatus;
import org.springframework.stereotype.Component;

@Component
public class MeterRequestMapper {

    public static MeterRequest toEntity(MeterRequestDTO dto, User user, Feeder feeder) {
        MeterRequest meterRequest = new MeterRequest();
        meterRequest.setUser(user);
        meterRequest.setMeterTypeRequested(MeterType.valueOf(dto.getMeterType()));
        meterRequest.setConnectionAddress(dto.getConnectionAddress());
        meterRequest.setFeeder(feeder);
        meterRequest.setReasonForRequest(dto.getReasonForRequest());
        meterRequest.setStatus(RequestStatus.PENDING);
        return meterRequest;
    }

    public static MeterRequestResponseDTO toDTO(MeterRequest meterRequest) {
        if (meterRequest == null) {
            return null;
        }

        return MeterRequestResponseDTO.builder()
                .id(meterRequest.getId())
                .meterType(meterRequest.getMeterTypeRequested().name())
                .connectionAddress(meterRequest.getConnectionAddress())
                .feederName(meterRequest.getFeeder() != null ? meterRequest.getFeeder().getFeederName() : null)
                .status(meterRequest.getStatus().name())
                .requestDate(meterRequest.getRequestDate())
                .approvedDate(meterRequest.getApprovedDate())
                .approvedByName(meterRequest.getApprovedBy() != null ? meterRequest.getApprovedBy().getName() : null)
                .assignedFieldStaffName(meterRequest.getAssignedFieldStaff() != null ?
                        meterRequest.getAssignedFieldStaff().getName() : null)
                .rejectionReason(meterRequest.getRejectionReason())
                .build();
    }
}