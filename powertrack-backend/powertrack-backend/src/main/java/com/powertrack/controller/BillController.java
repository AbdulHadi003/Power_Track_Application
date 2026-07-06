package com.powertrack.controller;

import com.powertrack.dto.request.bill.GenerateBillRequest;
import com.powertrack.dto.response.bill.BillResponseDTO;
import com.powertrack.service.BillService;
import com.powertrack.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;
    private final SecurityUtils securityUtils;

    @PostMapping("/generate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BillResponseDTO> generateBill(@Valid @RequestBody GenerateBillRequest request) {
        BillResponseDTO bill = billService.generateBill(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(bill);
    }

    @GetMapping("/my-bills")
    public ResponseEntity<List<BillResponseDTO>> getMyBills() {
        Long userId = securityUtils.getCurrentUserId();
        List<BillResponseDTO> bills = billService.getMyBills(userId);
        return ResponseEntity.ok(bills);
    }

    @GetMapping("/my-bills/unpaid")
    public ResponseEntity<List<BillResponseDTO>> getMyUnpaidBills() {
        Long userId = securityUtils.getCurrentUserId();
        List<BillResponseDTO> bills = billService.getMyUnpaidBills(userId);
        return ResponseEntity.ok(bills);
    }

    @GetMapping("/my-bills/outstanding")
    public ResponseEntity<BigDecimal> getTotalOutstanding() {
        Long userId = securityUtils.getCurrentUserId();
        BigDecimal outstanding = billService.getTotalOutstanding(userId);
        return ResponseEntity.ok(outstanding);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillResponseDTO> getBillById(@PathVariable Long id) {
        BillResponseDTO bill = billService.getBillById(id);
        return ResponseEntity.ok(bill);
    }

    @GetMapping("/meter/{meterId}")
    public ResponseEntity<List<BillResponseDTO>> getBillsByMeterId(@PathVariable Long meterId) {
        List<BillResponseDTO> bills = billService.getBillsByMeterId(meterId);
        return ResponseEntity.ok(bills);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BillResponseDTO>> getAllBills() {
        List<BillResponseDTO> bills = billService.getAllBills();
        return ResponseEntity.ok(bills);
    }

    @GetMapping("/unpaid")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BillResponseDTO>> getAllUnpaidBills() {
        List<BillResponseDTO> bills = billService.getAllUnpaidBills();
        return ResponseEntity.ok(bills);
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BillResponseDTO>> getOverdueBills() {
        List<BillResponseDTO> bills = billService.getOverdueBills();
        return ResponseEntity.ok(bills);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BillResponseDTO>> getBillsByStatus(@PathVariable String status) {
        List<BillResponseDTO> bills = billService.getBillsByStatus(status);
        return ResponseEntity.ok(bills);
    }

    @GetMapping("/eligible-installment")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BillResponseDTO>> getBillsEligibleForInstallment() {
        List<BillResponseDTO> bills = billService.getBillsEligibleForInstallment();
        return ResponseEntity.ok(bills);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BillResponseDTO> updateBill(
            @PathVariable Long id,
            @Valid @RequestBody GenerateBillRequest request) {
        BillResponseDTO bill = billService.updateBill(id, request);
        return ResponseEntity.ok(bill);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteBill(@PathVariable Long id) {
        billService.deleteBill(id);
        return ResponseEntity.ok("Bill deleted successfully");
    }

    @PostMapping("/apply-penalties")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> applyPenaltyToOverdueBills() {
        billService.applyPenaltyToOverdueBills();
        return ResponseEntity.ok("Penalties applied to overdue bills");
    }

    @GetMapping("/count/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> countBillsByStatus(@PathVariable String status) {
        long count = billService.countBillsByStatus(status);
        return ResponseEntity.ok(count);
    }
}