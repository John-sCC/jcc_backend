package com.nighthawk.spring_portfolio.mvc.statsData;

import java.util.List;

import lombok.Data;

@Data
public class TwoQuantitativeRequest {
    private List<Double> explanatory;
    private List<Double> response;
    private String explanatoryName; 
    private String responseName; 
}

