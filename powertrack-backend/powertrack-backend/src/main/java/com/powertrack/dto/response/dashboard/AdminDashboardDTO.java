package com.powertrack.dto.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardDTO {

    // User Statistics
    private Long totalUsers;
    private Long totalCustomers;
    private Long totalFieldStaff;
    private Long totalCSRs;

    // Meter Statistics
    private Long totalMeters;
    private Long activeMeters;
    private Long pendingMeterRequests;

    // Bill & Payment Statistics
    private Long totalBillsThisMonth;
    private Long unpaidBills;
    private BigDecimal totalRevenueThisMonth;
    private BigDecimal totalOutstandingAmount;

    // Installment Statistics
    private Long pendingInstallmentRequests;
    private Long activeInstallmentPlans;

    // Complaint Statistics
    private Long totalComplaints;
    private Long newComplaints;
    private Long inProgressComplaints;
    private Long pendingComplaints;
    private Long resolvedComplaints;

    // Chat Statistics
    private Long activeConversations;
    private Long waitingConversations;
}