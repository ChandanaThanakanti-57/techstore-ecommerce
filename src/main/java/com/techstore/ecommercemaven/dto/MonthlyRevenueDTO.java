package com.techstore.ecommercemaven.dto;

public class MonthlyRevenueDTO {

    private Integer month;
    private Double revenue;

    public MonthlyRevenueDTO(
            Integer month,
            Double revenue) {

        this.month = month;
        this.revenue = revenue;
    }

    public Integer getMonth() {
        return month;
    }

    public Double getRevenue() {
        return revenue;
    }
}