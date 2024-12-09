package com.nighthawk.spring_portfolio.mvc.statsData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TwoCategorical {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String explanatory;
    private String response;
    private int freq;
    private double relFreq;

    // Constructor to initialize the object with data
    public TwoCategorical(String explanatory, String response, int freq, double relFreq) {
        this.explanatory = explanatory;
        this.response = response;
        this.freq = freq;
        this.relFreq = relFreq;
    }

    // Getters and Setters
    public String getExplanatory() {
        return explanatory;
    }

    public void setExplanatory(String explanatory) {
        this.explanatory = explanatory;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public double getRelFreq() {
        return relFreq;
    }

    public void setRelFreq(double relFreq) {
        this.relFreq = relFreq;
    }

    // Method to calculate mean
    public static double calculateMean(List<Double> data) {
        double sum = 0.0;
        for (double num : data) {
            sum += num;
        }
        return sum / data.size();
    }

    // Method to calculate standard deviation
    public static double calculateSD(List<Double> data) {
        double mean = calculateMean(data);
        double sum = 0.0;
        for (double num : data) {
            sum += Math.pow(num - mean, 2);
        }
        return Math.sqrt(sum / data.size());
    }

    // Method to calculate median
    public static double getMedian(List<Double> data) {
        data.sort(Double::compareTo);
        int middle = data.size() / 2;
        if (data.size() % 2 == 0) {
            return (data.get(middle - 1) + data.get(middle)) / 2.0;
        } else {
            return data.get(middle);
        }
    }

    // Method to get minimum value
    public static double getMinimum(List<Double> data) {
        return data.stream().min(Double::compare).orElse(Double.NaN);
    }

    // Method to get maximum value
    public static double getMaximum(List<Double> data) {
        return data.stream().max(Double::compare).orElse(Double.NaN);
    }

    // Method to calculate correlation
    public static double calculateCorrelation(List<Double> data1, List<Double> data2) {
        double mean1 = calculateMean(data1);
        double mean2 = calculateMean(data2);
        double sumXY = 0.0, sumX2 = 0.0, sumY2 = 0.0;
        for (int i = 0; i < data1.size(); i++) {
            double x = data1.get(i) - mean1;
            double y = data2.get(i) - mean2;
            sumXY += x * y;
            sumX2 += x * x;
            sumY2 += y * y;
        }
        return sumXY / Math.sqrt(sumX2 * sumY2);
    }

    // Method to calculate standard error
    public static double calculateStandardError(List<Double> data) {
        return calculateSD(data) / Math.sqrt(data.size());
    }

    // Method to print data
    public void printData() {
        System.out.println("Explanatory: " + this.explanatory + 
                           ", Response: " + this.response + 
                           ", Frequency: " + this.freq + 
                           ", Relative Frequency: " + this.relFreq);
    }

    // Static method to initialize a sample TwoCategorical object
    public static TwoCategorical init() {
        String explanatory = "Explanatory Variable";
        String response = "Response Variable";
        int freq = 50;
        double relFreq = 0.5;

        TwoCategorical twoCategorical = new TwoCategorical(explanatory, response, freq, relFreq);
        twoCategorical.printData();

        return twoCategorical;
    }

    public static void main(String[] args) {
        TwoCategorical twoCategorical = init();
    }
}
