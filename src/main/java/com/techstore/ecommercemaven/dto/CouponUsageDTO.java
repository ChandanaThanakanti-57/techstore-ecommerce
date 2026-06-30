package com.techstore.ecommercemaven.dto;

public class CouponUsageDTO {

    private String code;
    private Long usageCount;

    public CouponUsageDTO(String code, Long usageCount) {
        this.code = code;
        this.usageCount = usageCount;
    }

    public String getCode() {
        return code;
    }

    public Long getUsageCount() {
        return usageCount;
    }
}