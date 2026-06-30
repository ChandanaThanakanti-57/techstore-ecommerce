package com.techstore.ecommercemaven.dto;

public class CouponRevenueDTO {

    private String couponCode;
    private Long usageCount;
    private Double revenueGenerated;

    public CouponRevenueDTO(
            String couponCode,
            Long usageCount,
            Double revenueGenerated) {

        this.couponCode = couponCode;
        this.usageCount = usageCount;
        this.revenueGenerated = revenueGenerated;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public Long getUsageCount() {
        return usageCount;
    }

    public Double getRevenueGenerated() {
        return revenueGenerated;
    }
}