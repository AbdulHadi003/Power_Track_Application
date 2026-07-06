package com.powertrack.service;

import com.powertrack.dto.mapper.InstallmentRequestMapper;
import com.powertrack.dto.request.installment.ApproveRejectInstallmentRequest;
import com.powertrack.dto.request.installment.InstallmentRequestDTO;
import com.powertrack.dto.request.installment.PayInstallmentRequest;
import com.powertrack.dto.response.installment.InstallmentResponseDTO;
import com.powertrack.entity.*;
import com.powertrack.enums.PaymentMethod;
import com.powertrack.enums.PaymentStatus;
import com.powertrack.enums.RequestStatus;
import com.powertrack.exception.ResourceNotFoundException;
import com.powertrack.repository.InstallmentPaymentRepository;
import com.powertrack.repository.InstallmentRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstallmentService {

    private final InstallmentRequestRepository installmentRequestRepository;
    private final InstallmentPaymentRepository installmentPaymentRepository;
    private final BillService billService;
    private final UserService userService;

    @Transactional
    public InstallmentResponseDTO requestInstallment(InstallmentRequestDTO request, Long userId) {
        Bill bill = billService.getBillEntityById(request.getBillId());
        User user = userService.getUserEntityById(userId);

        // Validate bill belongs to user
        if (!bill.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Bill does not belong to this user");
        }

        // Check if bill amount is >= 15000
        if (bill.getTotalAmount().compareTo(new BigDecimal("15000")) < 0) {
            throw new IllegalArgumentException("Bill amount must be at least Rs. 15,000 for installment");
        }

        // Check if installment already exists for this bill
        if (installmentRequestRepository.existsByBillId(request.getBillId())) {
            throw new IllegalStateException("Installment request already exists for this bill");
        }

        // Check if user has active installments
        long activeCount = installmentRequestRepository.countUserActiveInstallments(userId);
        if (activeCount > 0) {
            throw new IllegalStateException("User already has an active installment plan");
        }

        // Calculate monthly installment
        BigDecimal monthlyInstallment = bill.getTotalAmount()
                .divide(new BigDecimal(request.getNumberOfMonths()), 2, RoundingMode.HALF_UP);

        // Create installment request
        InstallmentRequest installmentRequest = new InstallmentRequest();
        installmentRequest.setUser(user);
        installmentRequest.setBill(bill);
        installmentRequest.setTotalAmount(bill.getTotalAmount());
        installmentRequest.setMonthlyInstallment(monthlyInstallment);
        installmentRequest.setNumberOfMonths(request.getNumberOfMonths());
        installmentRequest.setReasonForInstallment(request.getReasonForInstallment());
        installmentRequest.setStatus(RequestStatus.PENDING);
        installmentRequest.calculateDates();

        InstallmentRequest savedRequest = installmentRequestRepository.save(installmentRequest);
        return InstallmentRequestMapper.toDTO(savedRequest);
    }

    @Transactional(readOnly = true)
    public List<InstallmentResponseDTO> getAllInstallmentRequests() {
        return installmentRequestRepository.findAll().stream()
                .map(InstallmentRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InstallmentResponseDTO> getPendingInstallmentRequests() {
        return installmentRequestRepository.findAllPendingRequests().stream()
                .map(InstallmentRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InstallmentResponseDTO getInstallmentRequestById(Long id) {
        InstallmentRequest request = installmentRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InstallmentRequest", "id", id));
        return InstallmentRequestMapper.toDTO(request);
    }

    @Transactional(readOnly = true)
    public List<InstallmentResponseDTO> getMyInstallmentRequests(Long userId) {
        return installmentRequestRepository.findByUserId(userId).stream()
                .map(InstallmentRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public InstallmentResponseDTO approveOrRejectInstallment(Long requestId,
                                                             ApproveRejectInstallmentRequest request,
                                                             Long adminId) {
        InstallmentRequest installmentRequest = installmentRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("InstallmentRequest", "id", requestId));

        User admin = userService.getUserEntityById(adminId);

        if (request.getAction().equals("APPROVE")) {
            installmentRequest.setStatus(RequestStatus.APPROVED);
            installmentRequest.setApprovedBy(admin);
            installmentRequest.setApprovedDate(LocalDateTime.now());
            installmentRequest.calculateDates();

            // Generate installment payments
            generateInstallmentPayments(installmentRequest);

        } else if (request.getAction().equals("REJECT")) {
            if (request.getRejectionReason() == null || request.getRejectionReason().trim().isEmpty()) {
                throw new IllegalArgumentException("Rejection reason is required");
            }

            installmentRequest.setStatus(RequestStatus.REJECTED);
            installmentRequest.setApprovedBy(admin);
            installmentRequest.setApprovedDate(LocalDateTime.now());
            installmentRequest.setRejectionReason(request.getRejectionReason());
        }

        InstallmentRequest updatedRequest = installmentRequestRepository.save(installmentRequest);
        return InstallmentRequestMapper.toDTO(updatedRequest);
    }

    @Transactional
    public InstallmentResponseDTO payInstallment(PayInstallmentRequest request) {
        InstallmentPayment installmentPayment = installmentPaymentRepository.findById(request.getInstallmentPaymentId())
                .orElseThrow(() -> new ResourceNotFoundException("InstallmentPayment", "id",
                        request.getInstallmentPaymentId()));

        // Check if already paid
        if (installmentPayment.getStatus() == PaymentStatus.SUCCESS) {
            throw new IllegalStateException("This installment has already been paid");
        }

        // Process payment
        installmentPayment.setPaymentDate(LocalDate.now());
        installmentPayment.setStatus(PaymentStatus.SUCCESS);
        installmentPayment.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()));
        installmentPayment.generateReceiptNumber();

        installmentPaymentRepository.save(installmentPayment);

        // Check if all installments are paid
        InstallmentRequest installmentRequest = installmentPayment.getInstallmentRequest();
        long paidCount = installmentPaymentRepository.countPaidInstallments(installmentRequest.getId());

        if (paidCount == installmentRequest.getNumberOfMonths()) {
            installmentRequest.setStatus(RequestStatus.COMPLETED);
            installmentRequestRepository.save(installmentRequest);
        }

        return InstallmentRequestMapper.toDTO(installmentRequest);
    }

    @Transactional(readOnly = true)
    public long countPendingInstallmentRequests() {
        return installmentRequestRepository.countPendingRequests();
    }

    @Transactional
    public void deleteInstallmentRequest(Long id) {
        InstallmentRequest request = installmentRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InstallmentRequest", "id", id));

        // Only PENDING or REJECTED requests can be deleted
        if (request.getStatus() != RequestStatus.PENDING && request.getStatus() != RequestStatus.REJECTED) {
            throw new IllegalStateException("Only pending or rejected requests can be deleted");
        }

        installmentRequestRepository.delete(request);
    }

    // Helper method to generate installment payments
    private void generateInstallmentPayments(InstallmentRequest installmentRequest) {
        List<InstallmentPayment> payments = new ArrayList<>();
        LocalDate dueDate = installmentRequest.getStartDate();

        for (int i = 1; i <= installmentRequest.getNumberOfMonths(); i++) {
            InstallmentPayment payment = new InstallmentPayment();
            payment.setInstallmentRequest(installmentRequest);
            payment.setInstallmentNumber(i);
            payment.setAmount(installmentRequest.getMonthlyInstallment());
            payment.setDueDate(dueDate);
            payment.setStatus(PaymentStatus.PENDING);

            payments.add(payment);
            dueDate = dueDate.plusMonths(1);
        }

        installmentPaymentRepository.saveAll(payments);
    }
}