package com.techstore.ecommercemaven.service;

import com.techstore.ecommercemaven.model.Refund;
import com.techstore.ecommercemaven.repository.RefundRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RefundService {

    private final RefundRepository refundRepository;

    public RefundService(RefundRepository refundRepository) {
        this.refundRepository = refundRepository;
    }


    public Refund createRefund(Refund refund) {

        System.out.println("Saving refund for order: " + refund.getOrderId());

        refund.setStatus("PENDING");
        refund.setRefundDate(LocalDateTime.now());

        Refund saved = refundRepository.save(refund);

        System.out.println("Refund saved with ID: " + saved.getId());

        return saved;
    }


    public List<Refund> getAllRefunds() {

        return refundRepository.findAll();
    }


    public Refund getRefundById(Long id) {

        return refundRepository.findById(id).orElse(null);
    }


    public Refund completeRefund(Long id) {

        Refund refund = refundRepository.findById(id).orElse(null);

        if (refund != null) {

            refund.setStatus("COMPLETED");
            refund.setRefundDate(LocalDateTime.now());

            return refundRepository.save(refund);
        }

        return null;
    }


    public List<Refund> getRefundHistory(String email) {

        return refundRepository.findByUserEmail(email);
    }
}