package com.techstore.ecommercemaven.dto;

public class MonthlyOrdersDTO {

    private Integer month;
    private Long totalOrders;

    public MonthlyOrdersDTO(
            Integer month,
            Long totalOrders) {

        this.month = month;
        this.totalOrders = totalOrders;
    }

    public Integer getMonth() {
        return month;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

}
