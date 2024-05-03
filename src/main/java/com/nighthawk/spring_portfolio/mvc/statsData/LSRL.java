package com.nighthawk.spring_portfolio.mvc.statsData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LSRL extends StatsFunctions{

    private double slope;
    private double intercept;
    private double stdDev;
    private double correlationSqr;

    public LSRL(List<List<Double>> lists, double correlation){
        this.correlationSqr = correlation * correlation;

        this.slope = calculateSlope(lists.get(0), lists.get(1));
        this.intercept = calculateIntercept(lists.get(0), lists.get(1), this.slope);
    }

    public double calculateSlope(List<Double> x, List<Double> y) {
        double xMean = calculateMean(x);
        double yMean = calculateMean(y);
        
        double numerator = 0;
        double denominator = 0;
        
        for (int i = 0; i < x.size(); i++) {
            numerator += (x.get(i) - xMean) * (y.get(i) - yMean);
            denominator += Math.pow(x.get(i) - xMean, 2);
        }
        
        return numerator / denominator;
    }
    
    public double calculateIntercept(List<Double> x, List<Double> y, double slope) {
        double xMean = calculateMean(x);
        double yMean = calculateMean(y);
        
        return yMean - slope * xMean;
    }

}