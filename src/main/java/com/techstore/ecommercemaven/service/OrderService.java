package com.techstore.ecommercemaven.service;

import com.techstore.ecommercemaven.model.Order;
import com.techstore.ecommercemaven.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {


    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    public long getTotalOrders() {
        return orderRepository.count();
    }

    public double getTotalRevenue() {

        Double revenue = orderRepository.getTotalRevenue();

        return revenue == null ? 0 : revenue;
    }

    public List<Order> getOrdersByUserEmail(String email) {
        return orderRepository.findByUserEmail(email);
    }
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
}
