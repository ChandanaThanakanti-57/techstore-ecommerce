package com.techstore.ecommercemaven.service;

import com.techstore.ecommercemaven.model.Order;
import com.techstore.ecommercemaven.model.ReturnRequest;
import com.techstore.ecommercemaven.repository.ReturnRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReturnRequestService {

    private final ReturnRequestRepository repository;

    public ReturnRequestService(ReturnRequestRepository repository) {
        this.repository = repository;
    }

    public boolean createReturnRequest(
            Order order,
            String reason,
            String comments) {

        // Prevent duplicate requests
        if (repository.findByOrder(order) != null) {
            return false;
        }

        ReturnRequest request = new ReturnRequest();

        request.setOrder(order);
        request.setCustomerName(order.getCustomerName());
        request.setUserEmail(order.getUserEmail());
        request.setReason(reason);
        request.setComments(comments);
        request.setStatus("PENDING");
        request.setRequestDate(LocalDateTime.now());

        repository.save(request);

        return true;
    }

    public List<ReturnRequest> getCustomerRequests(String email) {
        return repository.findByUserEmail(email);
    }

    public List<ReturnRequest> getAllRequests() {
        return repository.findAll();
    }

    public ReturnRequest getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void updateStatus(Long id, String status) {

        ReturnRequest request = getById(id);

        if (request != null) {
            request.setStatus(status);
            repository.save(request);
        }
    }
}