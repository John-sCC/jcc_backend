package com.nighthawk.spring_portfolio.mvc.statsData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.*;

public class StatsFunctions {
    public double calculateMean(List<Double> dataset)
    {
        double sum = 0;
        for (double i : dataset)
        {
            sum += i;
        }
        double mean = sum / dataset.size();
        return mean;
    }

    public double calculateSD(List<Double> dataset)
    {
        double mean = calculateMean(dataset);
        double squaredDifferencesSum = 0;
        for (double i : dataset) {
            squaredDifferencesSum += Math.pow(i - mean, 2);
        }
        
        double variance = squaredDifferencesSum / dataset.size();
        
        double standardDeviation = Math.sqrt(variance);
        
        return standardDeviation;
    }

    public double getMinimum(List<Double> dataset)
    {
        dataset = this.customSort(dataset);
        return dataset.get(0);
    }

    public double getMaximum(List<Double> dataset)
    {
        dataset = this.customSort(dataset);
        return dataset.get(dataset.size()-1);
    }

    public double getMedian(List<Double> dataset)
    {
        dataset = this.customSort(dataset);
        if (dataset.size() == 1){
            return dataset.get(0);
        }
        else if (dataset.size() == 2){
            double avg = dataset.get(0) + dataset.get(1);
            avg /= 2;
            return avg;
        }
        else if (dataset.size() % 2 == 0)
        {
            double avg = dataset.get(dataset.size()/2) + dataset.get(dataset.size()/2 + 1);
            avg /= 2;
            return avg;
        }
        else
        {
            return dataset.get(dataset.size()/2 + 1);
        }
    }

    public double getQuartileOne(List<Double> dataset)
    {
        dataset = this.customSort(dataset);

        int midIndex = dataset.size() + 1;
        midIndex /= 2;
        
        // If the data length is odd, exclude the median from the lower half
        if (dataset.size() % 2 != 0) {
            midIndex--;
        }
        
        // Copy the lower half to a new array
        List<Double> lowerHalf = dataset.subList(0, midIndex);
        return getMedian(lowerHalf);
    }

    public double getQuartileThree(List<Double> dataset)
    {
        dataset = this.customSort(dataset);

        int midIndex = dataset.size() / 2;
        
        // If the data length is odd, exclude the median from the lower half
        if (dataset.size() % 2 != 0) {
            midIndex++;
        }
        
        // Copy the lower half to a new array
        List<Double> lowerHalf = dataset.subList(midIndex, dataset.size());
        return getMedian(lowerHalf);
    }



    public double calculateCorrelation(List<Double> x, List<Double> y) {
        if (x.size() != y.size()) {
            throw new IllegalArgumentException("Lists must be of equal length");
        }
        
        int n = x.size();
        
        // Calculate the mean of x and y
        double meanX = calculateMean(x);
        double meanY = calculateMean(y);
        
        // Calculate the sums of the products of deviations
        double sumXY = 0;
        double sumX2 = 0;
        double sumY2 = 0;
        
        for (int i = 0; i < n; i++) {
            double devX = x.get(i) - meanX;
            double devY = y.get(i) - meanY;
            sumXY += devX * devY;
            sumX2 += devX * devX;
            sumY2 += devY * devY;
        }

        double correlation = sumXY / Math.sqrt(sumX2 * sumY2);
        
        return correlation;
    }

    public List<Double> customSort(List<Double> arr){
        int n = arr.size();
        for (int i = 1; i < n; ++i) {
            double key = arr.get(i);
            int j = i - 1;

            while (j >= 0 && arr.get(j) > key) {
                arr.set(j + 1, arr.get(j));
                j = j - 1;
            }
            arr.set(j + 1, key);
        }
        return arr;
    }

}
