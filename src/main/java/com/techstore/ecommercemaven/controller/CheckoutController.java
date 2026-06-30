package com.techstore.ecommercemaven.controller;

import com.techstore.ecommercemaven.model.CartItem;
import com.techstore.ecommercemaven.model.Order;
import com.techstore.ecommercemaven.model.OrderItem;
import com.techstore.ecommercemaven.model.Product;
import com.techstore.ecommercemaven.model.User;
import com.techstore.ecommercemaven.repository.ProductRepository;
import com.techstore.ecommercemaven.service.CartService;
import com.techstore.ecommercemaven.service.OrderService;
import com.techstore.ecommercemaven.service.OrderItemService;
import com.techstore.ecommercemaven.repository.CouponRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.techstore.ecommercemaven.model.Coupon;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.techstore.ecommercemaven.service.EmailService;
import com.techstore.ecommercemaven.service.ProductService;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class CheckoutController {

    private final OrderService orderService;
    private final CartService cartService;
    private final ProductRepository productRepository;
    private final OrderItemService orderItemService;
    private final CouponRepository couponRepository;
    private final EmailService emailService;
    private final ProductService productService;
    public CheckoutController(
            OrderService orderService,
            CartService cartService,
            ProductRepository productRepository,
             OrderItemService orderItemService,
            CouponRepository couponRepository,
            EmailService emailService,
            ProductService productService) {

        this.orderService = orderService;
        this.cartService = cartService;
        this.productRepository = productRepository;
        this.orderItemService = orderItemService;
        this.couponRepository = couponRepository;
        this.emailService = emailService;
        this.productService = productService;
    }


    @GetMapping("/checkout")
    public String checkout() {
        return "checkout";
    }

    @PostMapping("/place-order")
    public String placeOrder(

            @RequestParam String customerName,
            @RequestParam String address,
            @RequestParam String phoneNumber,
            @RequestParam(required = false)
            String couponCode,

            @RequestParam String razorpayPaymentId,

            @RequestParam String razorpayOrderId,

            HttpSession session) {

        double total = cartService.getTotal();

        if (couponCode != null && !couponCode.isBlank()) {

            Coupon coupon =
                    couponRepository.findByCode(couponCode);

            if (coupon != null && coupon.isActive()) {

                total =
                        total -
                                (total * coupon.getDiscountPercent() / 100);

            }
        }

        List<CartItem> cartItems =
                cartService.getAllItems();

        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        Order order = new Order();

        order.setCustomerName(customerName);
        order.setAddress(address);
        order.setPhoneNumber(phoneNumber);
        order.setTotalAmount(total);
        order.setCouponCode(couponCode);
        order.setStatus("Paid");
        order.setOrderDate(LocalDateTime.now());

        User user =
                (User) session.getAttribute("loggedInUser");

        if (user != null) {
            order.setUserEmail(user.getEmail());
        }

// Reduce stock
        for (CartItem item : cartItems) {

            Product product =
                    productRepository.findById(
                                    item.getProductId())
                            .orElse(null);

            if (product != null) {

                Integer stock = product.getStock();

                if (stock == null) {
                    stock = 0;
                }

                int newStock =
                        stock - item.getQuantity();

                if (newStock < 0) {
                    newStock = 0;
                }

                product.setStock(newStock);

                productService.saveProduct(product);
            }
        }

// Save order first

        orderService.saveOrder(order);

        /*Payment payment =
                new Payment();

        payment.setOrderId(
                order.getId());

        payment.setRazorpayPaymentId(
                razorpayPaymentId);

        payment.setRazorpayOrderId(
                razorpayOrderId);

        payment.setStatus(
                "SUCCESS");

        payment.setAmount(
                order.getTotalAmount());

        paymentRepository.save(payment);*/

        if(user != null){

            emailService.sendOrderConfirmation(
                    user.getEmail(),
                    order.getId(),
                    order.getTotalAmount());
        }

        emailService.sendOrderConfirmation(
                order.getUserEmail(),
                order.getId(),
                order.getTotalAmount()
        );

// Save order items
        for (CartItem cartItem : cartItems) {

            OrderItem item = new OrderItem();

            item.setProductId(
                    cartItem.getProductId());

            item.setProductName(
                    cartItem.getProductName());

            item.setPrice(
                    cartItem.getPrice());

            item.setQuantity(
                    cartItem.getQuantity());

            item.setOrder(order);

            orderItemService.save(item);
        }

        cartService.clearCart();

        return "order-success";

    }
    @ResponseBody
    @GetMapping("/test-email")
    public String testEmail() {

        emailService.sendOrderConfirmation(
                "vandanathanakanti123@gmail.com",
                999L,
                1000);

        return "Email Sent";
    }
}