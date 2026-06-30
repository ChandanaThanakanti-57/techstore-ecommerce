package com.techstore.ecommercemaven.service;

import com.techstore.ecommercemaven.model.OrderItem;
import com.techstore.ecommercemaven.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(
            OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public void save(OrderItem item) {
        orderItemRepository.save(item);
    }
}