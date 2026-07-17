package com.techstore.ecommercemaven.controller;

import com.techstore.ecommercemaven.model.Order;
import com.techstore.ecommercemaven.model.User;
import com.techstore.ecommercemaven.service.OrderService;
import com.techstore.ecommercemaven.service.ReturnRequestService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReturnRequestController {

    private final ReturnRequestService returnRequestService;
    private final OrderService orderService;

    public ReturnRequestController(
            ReturnRequestService returnRequestService,
            OrderService orderService) {

        this.returnRequestService = returnRequestService;
        this.orderService = orderService;
    }


    // CUSTOMER - Open return request page
    @GetMapping("/request-return/{orderId}")
    public String showReturnPage(
            @PathVariable Long orderId,
            Model model,
            HttpSession session) {

        User user =
                (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        Order order =
                orderService.getOrderById(orderId);

        if (order == null ||
                !order.getUserEmail()
                        .equals(user.getEmail())) {

            return "redirect:/my-orders";
        }

        model.addAttribute("order", order);

        return "return-request";
    }


    // CUSTOMER - Submit return request
    @PostMapping("/request-return/{orderId}")
    public String submitReturn(
            @PathVariable Long orderId,
            @RequestParam String reason,
            @RequestParam String comments,
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        Order order =
                orderService.getOrderById(orderId);

        if (order == null) {
            return "redirect:/my-orders";
        }


        if (!"Delivered".equals(order.getStatus())) {

            return "redirect:/order-details/" + orderId;
        }


        boolean created =
                returnRequestService.createReturnRequest(
                        order,
                        reason,
                        comments);


        if (!created) {

            model.addAttribute(
                    "error",
                    "Return request already exists.");

            model.addAttribute("order", order);

            return "return-request";
        }


        return "redirect:/my-orders";
    }
}