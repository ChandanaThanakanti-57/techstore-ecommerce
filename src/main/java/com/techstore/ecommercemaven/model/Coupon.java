package com.techstore.ecommercemaven.model;

import jakarta.persistence.*;

@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private double discountPercent;

    private boolean active;

    public Coupon() {
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public boolean isActive() {
        return active;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}