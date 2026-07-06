package com.powertrack.controller;

import com.powertrack.dto.response.dashboard.AdminDashboardDTO;
import com.powertrack.dto.response.dashboard.CustomerDashboardDTO;
import com.powertrack.service.DashboardService;
import com.powertrack.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final SecurityUtils securityUtils;

    @GetMapping("/customer")
    public ResponseEntity<CustomerDashboardDTO> getCustomerDashboard() {
        Long userId = securityUtils.getCurrentUserId();
        CustomerDashboardDTO dashboard = dashboardService.getCustomerDashboard(userId);
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDashboardDTO> getAdminDashboard() {
        AdminDashboardDTO dashboard = dashboardService.getAdminDashboard();
        return ResponseEntity.ok(dashboard);
    }

    // Individual statistics endpoints

    @GetMapping("/stats/revenue/total")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BigDecimal> getTotalRevenueThisMonth() {
        BigDecimal revenue = dashboardService.getTotalRevenueThisMonth();
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/stats/revenue/monthly")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BigDecimal> getMonthlyRevenue(
            @RequestParam int month,
            @RequestParam int year) {
        BigDecimal revenue = dashboardService.getMonthlyRevenue(month, year);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/stats/customers/total")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getTotalCustomers() {
        long count = dashboardService.getTotalCustomers();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/meters/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getActiveMetersCount() {
        long count = dashboardService.getActiveMetersCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/bills/unpaid")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getUnpaidBillsCount() {
        long count = dashboardService.getUnpaidBillsCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/complaints/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getPendingComplaintsCount() {
        long count = dashboardService.getPendingComplaintsCount();
        return ResponseEntity.ok(count);
    }
}