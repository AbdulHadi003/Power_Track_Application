package com.powertrack.dto.response.dashboard;

import com.powertrack.dto.response.bill.BillResponseDTO;
import com.powertrack.dto.response.complaint.ComplaintResponseDTO;
import com.powertrack.dto.response.loadshedding.LoadSheddingScheduleDTO;
import com.powertrack.dto.response.meter.MeterResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDashboardDTO {
    private List<MeterResponseDTO> meters;
    private Integer totalMeters;

    private List<BillResponseDTO> currentBills;
    private BigDecimal totalOutstanding;
    private Integer unpaidBillsCount;

    private List<ComplaintResponseDTO> recentComplaints;
    private Integer openComplaintsCount;

    private LoadSheddingScheduleDTO todayLoadShedding;
    private List<LoadSheddingScheduleDTO> weekSchedules;

    private Integer unreadNotificationsCount;
}