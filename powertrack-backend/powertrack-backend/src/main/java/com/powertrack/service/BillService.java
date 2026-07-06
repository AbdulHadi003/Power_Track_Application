package com.powertrack.service;

import com.powertrack.dto.mapper.BillMapper;
import com.powertrack.dto.request.bill.GenerateBillRequest;
import com.powertrack.dto.response.bill.BillResponseDTO;
import com.powertrack.entity.*;
import com.powertrack.enums.BillStatus;
import com.powertrack.exception.ResourceNotFoundException;
import com.powertrack.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;
    private final MeterService meterService;
    private final TariffService tariffService;
    private final MeterReadingService meterReadingService;

    @Transactional
    public BillResponseDTO generateBill(GenerateBillRequest request) {
        Meter meter = meterService.getMeterEntityById(request.getMeterId());

        // Check if bill already exists for this month/year
        if (billRepository.existsByMeterIdAndBillingMonthAndBillingYear(
                request.getMeterId(), request.getBillingMonth(), request.getBillingYear())) {
            throw new IllegalStateException("Bill already exists for this meter in " +
                    request.getBillingMonth() + " " + request.getBillingYear());
        }

        // Get current active tariff
        Tariff tariff = tariffService.getCurrentActiveTariffEntity();

        // Calculate bill amounts
        BigDecimal unitRate = tariff.getUnitPrice();
        BigDecimal consumptionCharges = unitRate.multiply(new BigDecimal(request.getUnitsConsumed()));
        BigDecimal fixedCharges = tariff.getFixedCharge();
        BigDecimal taxAmount = consumptionCharges.multiply(tariff.getTaxPercentage())
                .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
        BigDecimal totalAmount = consumptionCharges.add(fixedCharges).add(taxAmount);

        // Create bill
        Bill bill = new Bill();
        bill.setUser(meter.getUser());
        bill.setMeter(meter);
        bill.setBillingMonth(request.getBillingMonth());
        bill.setBillingYear(request.getBillingYear());
        bill.setUnitsConsumed(request.getUnitsConsumed());
        bill.setUnitRate(unitRate);
        bill.setFixedCharges(fixedCharges);
        bill.setTaxAmount(taxAmount);
        bill.setPenaltyAmount(BigDecimal.ZERO);
        bill.setTotalAmount(totalAmount);
        bill.setDueDate(LocalDate.now().plusDays(15)); // 15 days to pay
        bill.setStatus(BillStatus.UNPAID);

        // Link to meter reading if provided
        if (request.getMeterReadingId() != null) {
            MeterReading meterReading = meterReadingService.getMeterReadingEntityById(request.getMeterReadingId());
            bill.setMeterReading(meterReading);
        }

        Bill savedBill = billRepository.save(bill);
        return BillMapper.toDTO(savedBill);
    }

    @Transactional(readOnly = true)
    public List<BillResponseDTO> getAllBills() {
        return billRepository.findAll().stream()
                .map(BillMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BillResponseDTO getBillById(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill", "id", id));
        return BillMapper.toDTO(bill);
    }

    @Transactional(readOnly = true)
    public List<BillResponseDTO> getMyBills(Long userId) {
        return billRepository.findByUserId(userId).stream()
                .map(BillMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BillResponseDTO> getBillsByMeterId(Long meterId) {
        return billRepository.findByMeterId(meterId).stream()
                .map(BillMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BillResponseDTO> getMyUnpaidBills(Long userId) {
        return billRepository.findUserUnpaidBills(userId).stream()
                .map(BillMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BillResponseDTO> getBillsByStatus(String status) {
        BillStatus billStatus = BillStatus.valueOf(status);
        return billRepository.findByStatus(billStatus).stream()
                .map(BillMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BillResponseDTO> getAllUnpaidBills() {
        return billRepository.findAllUnpaidBills().stream()
                .map(BillMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BillResponseDTO> getOverdueBills() {
        return billRepository.findOverdueBills().stream()
                .map(BillMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BillResponseDTO> getBillsEligibleForInstallment() {
        return billRepository.findBillsEligibleForInstallment().stream()
                .map(BillMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalOutstanding(Long userId) {
        BigDecimal total = billRepository.getTotalOutstandingByUserId(userId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Transactional
    public BillResponseDTO markBillAsPaid(Long billId, LocalDate paymentDate, String paymentMethod) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill", "id", billId));

        bill.setStatus(BillStatus.PAID);
        bill.setPaymentDate(paymentDate);
        bill.setPaymentMethod(paymentMethod);

        Bill updatedBill = billRepository.save(bill);
        return BillMapper.toDTO(updatedBill);
    }

    @Transactional
    public void applyPenaltyToOverdueBills() {
        List<Bill> overdueBills = billRepository.findOverdueBills();
        Tariff tariff = tariffService.getCurrentActiveTariffEntity();

        for (Bill bill : overdueBills) {
            if (bill.getPenaltyAmount().compareTo(BigDecimal.ZERO) == 0) {
                BigDecimal penalty = bill.getTotalAmount()
                        .multiply(tariff.getPenaltyPercentage())
                        .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);

                bill.setPenaltyAmount(penalty);
                bill.setTotalAmount(bill.getTotalAmount().add(penalty));
                bill.setStatus(BillStatus.OVERDUE);
                billRepository.save(bill);
            }
        }
    }

    @Transactional
    public BillResponseDTO updateBill(Long id, GenerateBillRequest request) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill", "id", id));

        // Only UNPAID bills can be updated
        if (bill.getStatus() != BillStatus.UNPAID) {
            throw new IllegalStateException("Only unpaid bills can be updated");
        }

        // Recalculate amounts
        Tariff tariff = tariffService.getCurrentActiveTariffEntity();
        BigDecimal unitRate = tariff.getUnitPrice();
        BigDecimal consumptionCharges = unitRate.multiply(new BigDecimal(request.getUnitsConsumed()));
        BigDecimal fixedCharges = tariff.getFixedCharge();
        BigDecimal taxAmount = consumptionCharges.multiply(tariff.getTaxPercentage())
                .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
        BigDecimal totalAmount = consumptionCharges.add(fixedCharges).add(taxAmount);

        bill.setUnitsConsumed(request.getUnitsConsumed());
        bill.setUnitRate(unitRate);
        bill.setFixedCharges(fixedCharges);
        bill.setTaxAmount(taxAmount);
        bill.setTotalAmount(totalAmount);

        Bill updatedBill = billRepository.save(bill);
        return BillMapper.toDTO(updatedBill);
    }

    @Transactional
    public void deleteBill(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill", "id", id));

        // Only UNPAID bills can be deleted
        if (bill.getStatus() != BillStatus.UNPAID) {
            throw new IllegalStateException("Only unpaid bills can be deleted");
        }

        billRepository.delete(bill);
    }

    @Transactional(readOnly = true)
    public long countBillsByStatus(String status) {
        BillStatus billStatus = BillStatus.valueOf(status);
        return billRepository.countByStatus(billStatus);
    }

    // Helper method to get Bill entity (for internal use)
    @Transactional(readOnly = true)
    public Bill getBillEntityById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill", "id", id));
    }
}