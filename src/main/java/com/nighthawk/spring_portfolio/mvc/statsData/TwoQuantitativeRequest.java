package com.nighthawk.spring_portfolio.mvc.statsData;

import java.util.List;

import lombok.Data;

@Data
public class TwoQuantitativeRequest {
    private List<Double> data1;
    private List<Double> data2;
    private String name1; 
    private String name2; 
}

