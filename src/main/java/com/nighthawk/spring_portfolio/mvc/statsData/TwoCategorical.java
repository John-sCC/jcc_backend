package com.nighthawk.spring_portfolio.mvc.statsData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity // Annotation to simplify creating an entity, which is a lightweight persistence domain object. Typically, an entity represents a table in a relational database, and each entity instance corresponds to a row in that table.
public class TwoCategorical extends StatsFunctions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String explanatoryName;
    private String responseName;

    @Lob
    private double[][] frequencies;

    public TwoCategorical(String explanatoryName, String responseName, double[][] frequencies) {
        this.explanatoryName = explanatoryName;
        this.responseName = responseName;
        this.frequencies = frequencies;
    }

    // Method to calculate relative frequencies for the two-way table
    public double[][] calculateRelativeFrequencies() {
        double total = Arrays.stream(frequencies)
                             .flatMapToDouble(Arrays::stream)
                             .sum();
        double[][] relativeFrequencies = new double[frequencies.length][frequencies[0].length];
        for (int i = 0; i < frequencies.length; i++) {
            for (int j = 0; j < frequencies[i].length; j++) {
                relativeFrequencies[i][j] = frequencies[i][j] / total;
            }
        }
        return relativeFrequencies;
    }

    // Method to generate a bar chart for the two-way table
    public void generateBarChart(String chartTitle, String categoryAxisLabel, String valueAxisLabel, String filePath) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < frequencies.length; i++) {
            for (int j = 0; j < frequencies[i].length; j++) {
                dataset.addValue(frequencies[i][j], explanatoryName + " " + i, responseName + " " + j);
            }
        }

        JFreeChart barChart = ChartFactory.createBarChart(
            chartTitle,
            categoryAxisLabel,
            valueAxisLabel,
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false);

        int width = 640; /* Width of the image */
        int height = 480; /* Height of the image */
        File barChartFile = new File(filePath);
        ChartUtils.saveChartAsJPEG(barChartFile, barChart, width, height);
    }

    // Method to flatten the frequencies array into a single list of doubles
    private double[] flattenFrequencies() {
        return Arrays.stream(frequencies).flatMapToDouble(Arrays::stream).toArray();
    }

    // Method to calculate mean
    public double calculateMean() {
        double[] flatFrequencies = flattenFrequencies();
        return Arrays.stream(flatFrequencies).average().orElse(0.0);
    }

    // Method to calculate standard deviation
    public double calculateSD() {
        double[] flatFrequencies = flattenFrequencies();
        double mean = calculateMean();
        return Math.sqrt(Arrays.stream(flatFrequencies).map(f -> Math.pow(f - mean, 2)).average().orElse(0.0));
    }

    // Method to calculate median
    public double getMedian() {
        double[] flatFrequencies = flattenFrequencies();
        Arrays.sort(flatFrequencies);
        int middle = flatFrequencies.length / 2;
        if (flatFrequencies.length % 2 == 0) {
            return (flatFrequencies[middle - 1] + flatFrequencies[middle]) / 2.0;
        } else {
            return flatFrequencies[middle];
        }
    }

    // Method to get minimum value
    public double getMinimum() {
        return Arrays.stream(flattenFrequencies()).min().orElse(0.0);
    }

    // Method to get maximum value
    public double getMaximum() {
        return Arrays.stream(flattenFrequencies()).max().orElse(0.0);
    }

    // Method to calculate quartile one
    public double getQuartileOne() {
        double[] flatFrequencies = flattenFrequencies();
        Arrays.sort(flatFrequencies);
        return flatFrequencies[flatFrequencies.length / 4];
    }

    // Method to calculate quartile three
    public double getQuartileThree() {
        double[] flatFrequencies = flattenFrequencies();
        Arrays.sort(flatFrequencies);
        return flatFrequencies[(flatFrequencies.length * 3) / 4];
    }

    // Override toString method to handle the array printing properly
    @Override
    public String toString() {
        return "TwoCategorical{" +
                "id=" + id +
                ", explanatoryName='" + explanatoryName + '\'' +
                ", responseName='" + responseName + '\'' +
                ", frequencies=" + Arrays.deepToString(frequencies) +
                '}';
    }

    public void printData() {
        System.out.println("Data: " + Arrays.deepToString(frequencies) +
                "\nExplanatory Name: " + explanatoryName +
                "\nResponse Name: " + responseName +
                "\nMean: " + calculateMean() +
                "\nStandard Deviation: " + calculateSD() +
                "\nMin: " + getMinimum() +
                "\nMax: " + getMaximum() +
                "\nMedian: " + getMedian() +
                "\nQ1: " + getQuartileOne() +
                "\nQ3: " + getQuartileThree());
    }

    public static TwoCategorical init() {
        double[][] frequencies = {
                {10.0, 20.0, 30.0},
                {15.0, 25.0, 35.0}
        };

        TwoCategorical twoCat = new TwoCategorical("Explanatory", "Response", frequencies);
        twoCat.printData();

        return twoCat;
    }

    public static void main(String[] args) {
        TwoCategorical twoCat = init();
    }
}
