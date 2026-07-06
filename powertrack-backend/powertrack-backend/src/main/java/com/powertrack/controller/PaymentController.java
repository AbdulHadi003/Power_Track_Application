package com.powertrack.controller;

import com.powertrack.dto.request.payment.PaymentRequest;
import com.powertrack.dto.response.payment.PaymentResponseDTO;
import com.powertrack.service.PaymentService;
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
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final SecurityUtils securityUtils;

    @PostMapping("/pay")
    public ResponseEntity<PaymentResponseDTO> processPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponseDTO payment = paymentService.processPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @GetMapping("/my-payments")
    public ResponseEntity<List<PaymentResponseDTO>> getMyPayments() {
        Long userId = securityUtils.getCurrentUserId();
        List<PaymentResponseDTO> payments = paymentService.getMyPayments(userId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable Long id) {
        PaymentResponseDTO payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/bill/{billId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByBillId(@PathVariable Long billId) {
        PaymentResponseDTO payment = paymentService.getPaymentByBillId(billId);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByTransactionId(@PathVariable String transactionId) {
        PaymentResponseDTO payment = paymentService.getPaymentByTransactionId(transactionId);
        return ResponseEntity.ok(payment);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        List<PaymentResponseDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByStatus(@PathVariable String status) {
        List<PaymentResponseDTO> payments = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/method/{method}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByMethod(@PathVariable String method) {
        List<PaymentResponseDTO> payments = paymentService.getPaymentsByMethod(method);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/recent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponseDTO>> getRecentPayments() {
        List<PaymentResponseDTO> payments = paymentService.getRecentPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/revenue/total")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BigDecimal> getTotalRevenue() {
        BigDecimal revenue = paymentService.getTotalRevenue();
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/revenue/monthly")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BigDecimal> getMonthlyRevenue(
            @RequestParam int month,
            @RequestParam int year) {
        BigDecimal revenue = paymentService.getMonthlyRevenue(month, year);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/count/successful")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> countSuccessfulPayments() {
        long count = paymentService.countSuccessfulPayments();
        return ResponseEntity.ok(count);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok("Payment deleted successfully");
    }
}