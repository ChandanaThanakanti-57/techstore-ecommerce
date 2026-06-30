package com.techstore.ecommercemaven.model;

import jakarta.persistence.*;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String razorpayOrderId;

    private String razorpayPaymentId;

    private String status;

    private double amount;

    private Long orderId;

    // getters setters
}