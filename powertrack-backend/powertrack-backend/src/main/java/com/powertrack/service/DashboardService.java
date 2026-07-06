package com.powertrack.service;

import com.powertrack.dto.response.bill.BillResponseDTO;
import com.powertrack.dto.response.complaint.ComplaintResponseDTO;
import com.powertrack.dto.response.dashboard.AdminDashboardDTO;
import com.powertrack.dto.response.dashboard.CustomerDashboardDTO;
import com.powertrack.dto.response.loadshedding.LoadSheddingScheduleDTO;
import com.powertrack.dto.response.meter.MeterResponseDTO;
import com.powertrack.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserService userService;
    private final MeterService meterService;
    private final MeterRequestService meterRequestService;
    private final BillService billService;
    private final PaymentService paymentService;
    private final InstallmentService installmentService;
    private final ComplaintService complaintService;
    private final ChatService chatService;
    private final LoadSheddingService loadSheddingService;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public AdminDashboardDTO getAdminDashboard() {
        // User Statistics
        long totalUsers = userService.getAllUsers().size();
        long totalCustomers = userService.getUsersByRole(UserRole.CUSTOMER.name()).size();
        long totalFieldStaff = userService.getUsersByRole(UserRole.FIELD_STAFF.name()).size();
        long totalCSRs = userService.getUsersByRole(UserRole.SUPPORT.name()).size();

        // Meter Statistics
        long totalMeters = meterService.getAllMeters().size();
        long activeMeters = meterService.countActiveMeters();
        long pendingMeterRequests = meterRequestService.countPendingRequests();

        // Bill & Payment Statistics
        long totalBillsThisMonth = billService.getAllBills().size();
        long unpaidBills = billService.countBillsByStatus("UNPAID");
        BigDecimal totalRevenueThisMonth = paymentService.getTotalRevenue();

        // Calculate total outstanding
        BigDecimal totalOutstanding = BigDecimal.ZERO;
        for (var user : userService.getAllUsers()) {
            totalOutstanding = totalOutstanding.add(billService.getTotalOutstanding(user.getId()));
        }

        // Installment Statistics
        long pendingInstallmentRequests = installmentService.countPendingInstallmentRequests();
        long activeInstallmentPlans = installmentService.getAllInstallmentRequests().stream()
                .filter(req -> req.getStatus().equals("APPROVED"))
                .count();

        // Complaint Statistics
        long totalComplaints = complaintService.getAllComplaints().size();
        long newComplaints = complaintService.countNewComplaints();
        long inProgressComplaints = complaintService.countComplaintsByStatus("IN_PROGRESS");
        long pendingComplaints = complaintService.countPendingComplaints();
        long resolvedComplaints = complaintService.countComplaintsByStatus("RESOLVED");

        // Chat Statistics
        long activeConversations = chatService.countActiveConversations();
        long waitingConversations = chatService.getWaitingConversations().size();

        return AdminDashboardDTO.builder()
                .totalUsers(totalUsers)
                .totalCustomers(totalCustomers)
                .totalFieldStaff(totalFieldStaff)
                .totalCSRs(totalCSRs)
                .totalMeters(totalMeters)
                .activeMeters(activeMeters)
                .pendingMeterRequests(pendingMeterRequests)
                .totalBillsThisMonth(totalBillsThisMonth)
                .unpaidBills(unpaidBills)
                .totalRevenueThisMonth(totalRevenueThisMonth)
                .totalOutstandingAmount(totalOutstanding)
                .pendingInstallmentRequests(pendingInstallmentRequests)
                .activeInstallmentPlans(activeInstallmentPlans)
                .totalComplaints(totalComplaints)
                .newComplaints(newComplaints)
                .inProgressComplaints(inProgressComplaints)
                .pendingComplaints(pendingComplaints)
                .resolvedComplaints(resolvedComplaints)
                .activeConversations(activeConversations)
                .waitingConversations(waitingConversations)
                .build();
    }

    @Transactional(readOnly = true)
    public CustomerDashboardDTO getCustomerDashboard(Long userId) {
        // Meter Information
        List<MeterResponseDTO> meters = meterService.getMyMeters(userId);
        int totalMeters = meters.size();

        // Bill Information
        List<BillResponseDTO> currentBills = billService.getMyUnpaidBills(userId);
        BigDecimal totalOutstanding = billService.getTotalOutstanding(userId);
        int unpaidBillsCount = currentBills.size();

        // Complaint Information
        List<ComplaintResponseDTO> recentComplaints = complaintService.getMyComplaints(userId);
        int openComplaintsCount = (int) recentComplaints.stream()
                .filter(c -> !c.getStatus().equals("CLOSED") && !c.getStatus().equals("RESOLVED"))
                .count();

        // Load Shedding Information
        List<LoadSheddingScheduleDTO> todaySchedules = loadSheddingService.getTodayLoadSheddingForUser(userId);
        LoadSheddingScheduleDTO todayLoadShedding = todaySchedules.isEmpty() ? null : todaySchedules.get(0);
        List<LoadSheddingScheduleDTO> weekSchedules = loadSheddingService.getMyLoadSheddingSchedule(userId);

        // Notifications
        int unreadNotificationsCount = (int) notificationService.countUnreadNotifications(userId);

        return CustomerDashboardDTO.builder()
                .meters(meters)
                .totalMeters(totalMeters)
                .currentBills(currentBills)
                .totalOutstanding(totalOutstanding)
                .unpaidBillsCount(unpaidBillsCount)
                .recentComplaints(recentComplaints)
                .openComplaintsCount(openComplaintsCount)
                .todayLoadShedding(todayLoadShedding)
                .weekSchedules(weekSchedules)
                .unreadNotificationsCount(unreadNotificationsCount)
                .build();
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalRevenueThisMonth() {
        return paymentService.getTotalRevenue();
    }

    @Transactional(readOnly = true)
    public BigDecimal getMonthlyRevenue(int month, int year) {
        return paymentService.getMonthlyRevenue(month, year);
    }

    @Transactional(readOnly = true)
    public long getTotalCustomers() {
        return userService.getUsersByRole(UserRole.CUSTOMER.name()).size();
    }

    @Transactional(readOnly = true)
    public long getActiveMetersCount() {
        return meterService.countActiveMeters();
    }

    @Transactional(readOnly = true)
    public long getUnpaidBillsCount() {
        return billService.countBillsByStatus("UNPAID");
    }

    @Transactional(readOnly = true)
    public long getPendingComplaintsCount() {
        return complaintService.countPendingComplaints();
    }
}