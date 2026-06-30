package com.techstore.ecommercemaven.dto;

public class CustomerGrowthDTO {

    private Integer month;
    private Long userCount;

    public CustomerGrowthDTO(
            Integer month,
            Long userCount) {

        this.month = month;
        this.userCount = userCount;
    }

    public Integer getMonth() {
        return month;
    }

    public Long getUserCount() {
        return userCount;
    }
}