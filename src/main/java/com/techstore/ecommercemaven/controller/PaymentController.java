/*package com.techstore.ecommercemaven.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final RazorpayClient razorpayClient;

    public PaymentController(
            RazorpayClient razorpayClient) {

        this.razorpayClient = razorpayClient;
    }
    @PostMapping("/create-order")
    public String createOrder(
            @RequestParam double amount)
            throws Exception {

        JSONObject options =
                new JSONObject();

        options.put(
                "amount",
                amount * 100);

        options.put(
                "currency",
                "INR");

        options.put(
                "receipt",
                "txn_" +
                        System.currentTimeMillis());

        Order order = razorpayClient.Orders.create(options);

        return order.toString();
    }
}*/