package com.techstore.ecommercemaven.dto;

public class SalesForecastDTO {

    private Double predictedRevenue;

    public SalesForecastDTO(Double predictedRevenue) {
        this.predictedRevenue = predictedRevenue;
    }

    public Double getPredictedRevenue() {
        return predictedRevenue;
    }
}