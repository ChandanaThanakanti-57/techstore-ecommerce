package com.techstore.ecommercemaven.dto;

public class WishlistProductDTO {

    private String productName;
    private Long totalWishlists;

    public WishlistProductDTO(
            String productName,
            Long totalWishlists) {

        this.productName = productName;
        this.totalWishlists = totalWishlists;
    }

    public String getProductName() {
        return productName;
    }

    public Long getTotalWishlists() {
        return totalWishlists;
    }
}