package com.techstore.ecommercemaven.dto;

public class TopCustomerDTO {

    private String userEmail;
    private Long totalOrders;
    private Double totalSpent;

    public TopCustomerDTO(
            String userEmail,
            Long totalOrders,
            Double totalSpent) {

        this.userEmail = userEmail;
        this.totalOrders = totalOrders;
        this.totalSpent = totalSpent;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public Double getTotalSpent() {
        return totalSpent;
    }
}