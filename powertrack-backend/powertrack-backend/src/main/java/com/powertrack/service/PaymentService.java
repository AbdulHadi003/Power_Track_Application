package com.powertrack.service;

import com.powertrack.dto.mapper.PaymentMapper;
import com.powertrack.dto.request.payment.PaymentRequest;
import com.powertrack.dto.response.payment.PaymentResponseDTO;
import com.powertrack.entity.Bill;
import com.powertrack.entity.Payment;
import com.powertrack.enums.BillStatus;
import com.powertrack.enums.PaymentMethod;
import com.powertrack.enums.PaymentStatus;
import com.powertrack.exception.ResourceNotFoundException;
import com.powertrack.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BillService billService;

    @Transactional
    public PaymentResponseDTO processPayment(PaymentRequest request) {
        Bill bill = billService.getBillEntityById(request.getBillId());

        // Check if bill is already paid
        if (paymentRepository.existsByBillIdAndStatus(request.getBillId(), PaymentStatus.SUCCESS)) {
            throw new IllegalStateException("Bill has already been paid");
        }

        // Validate payment amount
        if (request.getAmount().compareTo(bill.getTotalAmount()) != 0) {
            throw new IllegalArgumentException("Payment amount must match bill total amount");
        }

        // Create payment
        Payment payment = new Payment();
        payment.setBill(bill);
        payment.setAmountPaid(request.getAmount());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()));
        payment.setTransactionId(request.getTransactionId());
        payment.setStatus(PaymentStatus.SUCCESS); // In real app, would verify with gateway
        payment.generateReceiptNumber();

        Payment savedPayment = paymentRepository.save(payment);

        // Update bill status
        billService.markBillAsPaid(bill.getId(), savedPayment.getPaymentDate().toLocalDate(),
                request.getPaymentMethod());

        return PaymentMapper.toDTO(savedPayment);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(PaymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));
        return PaymentMapper.toDTO(payment);
    }

    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentByBillId(Long billId) {
        Payment payment = paymentRepository.findByBillId(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for bill"));
        return PaymentMapper.toDTO(payment);
    }

    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentByTransactionId(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "transactionId", transactionId));
        return PaymentMapper.toDTO(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getMyPayments(Long userId) {
        // Get all bills for user, then get their payments
        List<Bill> userBills = billService.getMyBills(userId).stream()
                .map(dto -> billService.getBillEntityById(dto.getId()))
                .collect(Collectors.toList());

        return userBills.stream()
                .flatMap(bill -> paymentRepository.findAllByBillId(bill.getId()).stream())
                .map(PaymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getPaymentsByStatus(String status) {
        PaymentStatus paymentStatus = PaymentStatus.valueOf(status);
        return paymentRepository.findByStatus(paymentStatus).stream()
                .map(PaymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getPaymentsByMethod(String method) {
        PaymentMethod paymentMethod = PaymentMethod.valueOf(method);
        return paymentRepository.findByPaymentMethod(paymentMethod).stream()
                .map(PaymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getRecentPayments() {
        return paymentRepository.findRecentPayments().stream()
                .map(PaymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalRevenue() {
        BigDecimal revenue = paymentRepository.getTotalRevenue();
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public BigDecimal getMonthlyRevenue(int month, int year) {
        BigDecimal revenue = paymentRepository.getMonthlyRevenue(month, year);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public long countSuccessfulPayments() {
        return paymentRepository.countSuccessfulPayments();
    }

    @Transactional
    public PaymentResponseDTO markPaymentAsFailed(Long paymentId, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));

        payment.setStatus(PaymentStatus.FAILED);
        payment.setGatewayResponse(reason);

        Payment updatedPayment = paymentRepository.save(payment);
        return PaymentMapper.toDTO(updatedPayment);
    }

    @Transactional
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));

        // Only FAILED payments can be deleted
        if (payment.getStatus() != PaymentStatus.FAILED) {
            throw new IllegalStateException("Only failed payments can be deleted");
        }

        paymentRepository.delete(payment);
    }
}