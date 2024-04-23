package com.nighthawk.spring_portfolio.mvc.statsData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

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

    public LSRL(Map<String, List<Double>> lists, double correlation){
        this.correlationSqr = correlation * correlation;

        Set<Map.Entry<String, List<Double>>> entrySet = lists.entrySet();

        if (!entrySet.isEmpty()) {
            // Using iterator
            Iterator<Map.Entry<String, List<Double>>> iterator = entrySet.iterator();
            Map.Entry<String, List<Double>> firstEntry = iterator.next();
            Map.Entry<String, List<Double>> secondEntry = iterator.next();
            this.slope = calculateSlope(firstEntry.getValue(), secondEntry.getValue());
            this.intercept = calculateIntercept(firstEntry.getValue(), secondEntry.getValue(), this.slope);
        }
        else {
            this.slope = 0;
        }

        
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