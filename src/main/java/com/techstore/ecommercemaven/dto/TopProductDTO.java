package com.techstore.ecommercemaven.dto;

public class TopProductDTO {

    private String productName;
    private Long totalSold;

    public TopProductDTO(
            String productName,
            Long totalSold) {

        this.productName = productName;
        this.totalSold = totalSold;
    }

    public String getProductName() {
        return productName;
    }

    public Long getTotalSold() {
        return totalSold;
    }

}
