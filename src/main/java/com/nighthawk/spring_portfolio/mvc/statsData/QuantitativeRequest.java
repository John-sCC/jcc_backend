package com.nighthawk.spring_portfolio.mvc.statsData;

import java.util.List;

import lombok.Data;

@Data
public class QuantitativeRequest {
    private List<Double> data;
    private String name; 
}

