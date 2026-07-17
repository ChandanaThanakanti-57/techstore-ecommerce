package com.techstore.ecommercemaven.repository;

import com.techstore.ecommercemaven.model.Order;
import com.techstore.ecommercemaven.model.ReturnRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReturnRequestRepository
        extends JpaRepository<ReturnRequest, Long> {

    List<ReturnRequest> findByUserEmail(String userEmail);

    List<ReturnRequest> findByStatus(String status);

    ReturnRequest findByOrder(Order order);

}