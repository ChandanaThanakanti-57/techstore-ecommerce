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
import com.techstore.ecommercemaven.repository.OrderItemRepository;
import com.techstore.ecommercemaven.repository.ProductRepository;
import com.techstore.ecommercemaven.model.Product;
import com.techstore.ecommercemaven.model.OrderItem;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Controller
public class MyOrdersController {


    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public MyOrdersController(
            OrderService orderService,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            ProductRepository productRepository) {

        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository =productRepository;

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

        List<OrderItem> orderItems =
                orderItemRepository.findByOrderId(id);

        model.addAttribute("order", order);
        model.addAttribute("orderItems", orderItems);

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
    @PostMapping("/cancel-order/{id}")
    public String cancelOrder(
            @PathVariable Long id,
            HttpSession session) {

        User user =
                (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        Order order =
                orderRepository.findById(id)
                        .orElse(null);

        if (order == null) {
            return "redirect:/my-orders";
        }

        if (!order.getUserEmail().equals(user.getEmail())) {
            return "redirect:/my-orders";
        }

        if (!"Paid".equals(order.getStatus())) {
            return "redirect:/order-details/" + id;
        }

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

        order.setStatus("Cancelled");

        orderRepository.save(order);

        return "redirect:/order-details/" + id;
    }
}
