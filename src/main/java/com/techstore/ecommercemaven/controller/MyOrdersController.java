package com.techstore.ecommercemaven.controller;
import com.techstore.ecommercemaven.model.Order;
import com.techstore.ecommercemaven.model.User;
import com.techstore.ecommercemaven.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.servlet.http.HttpSession;
import com.techstore.ecommercemaven.repository.OrderRepository;

@Controller
public class MyOrdersController {


    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public MyOrdersController(
            OrderService orderService,
            OrderRepository orderRepository) {

        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }
    @GetMapping("/my-orders")
    public String myOrders(
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "orders",
                orderService.getOrdersByUserEmail(
                        user.getEmail()));

        return "my-orders";
    }
    @GetMapping("/order-details/{id}")
    public String orderDetails(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        Order order = orderService.getOrderById(id);

        if (order == null) {
            return "redirect:/my-orders";
        }

        if (!order.getUserEmail()
                .equals(user.getEmail())) {

            return "redirect:/my-orders";
        }

        model.addAttribute("order", order);

        return "order-details";
    }
    @GetMapping("/track-order/{id}")
    public String trackOrder(
            @PathVariable Long id,
            Model model) {

        Order order =
                orderRepository.findById(id)
                        .orElse(null);

        model.addAttribute("order", order);

        return "order-tracking";
    }
}
