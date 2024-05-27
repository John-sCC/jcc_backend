package com.nighthawk.spring_portfolio.mvc.statsData;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import lombok.Data;

@Data
public class CategoricalRequest {
    private int size;
    private Map<String, Double> data;
}
