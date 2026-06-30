package com.techstore.ecommercemaven.controller;

import com.techstore.ecommercemaven.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String viewOrders(Model model) {

        model.addAttribute("orders",
                orderService.getAllOrders());

        return "orders";
    }
}