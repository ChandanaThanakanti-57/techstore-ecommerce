package com.techstore.ecommercemaven.dto;

public class CategorySalesDTO {

    private String category;
    private Long totalSold;

    public CategorySalesDTO(String category, Long totalSold) {
        this.category = category;
        this.totalSold = totalSold;
    }

    public String getCategory() {
        return category;
    }

    public Long getTotalSold() {
        return totalSold;
    }
}