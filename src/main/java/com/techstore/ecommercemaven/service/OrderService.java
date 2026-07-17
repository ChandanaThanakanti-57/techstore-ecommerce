package com.techstore.ecommercemaven.service;

import com.techstore.ecommercemaven.model.Order;
import com.techstore.ecommercemaven.model.OrderItem;
import com.techstore.ecommercemaven.model.Product;
import com.techstore.ecommercemaven.repository.OrderRepository;
import com.techstore.ecommercemaven.repository.OrderItemRepository;
import com.techstore.ecommercemaven.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderService {


    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            ProductRepository productRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
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

    public List<Order> getOrdersByUserEmail(String email) {

        return orderRepository
                .findByUserEmailOrderByOrderDateDesc(email);
    }

    public double getTotalRevenue() {

        Double revenue = orderRepository.getTotalRevenue();

        return revenue == null ? 0 : revenue;
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void restoreStock(Order order) {

        List<OrderItem> orderItems =
                orderItemRepository.findByOrderId(order.getId());

        for (OrderItem item : orderItems) {

            Product product =
                    productRepository.findById(item.getProductId())
                            .orElse(null);

            if (product != null) {

                int stock =
                        product.getStock() == null ? 0 : product.getStock();

                product.setStock(
                        stock + item.getQuantity());

                productRepository.save(product);
            }
        }
    }
}

