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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity // Annotation to simplify creating an entity, which is a lightweight persistence domain object. Typically, an entity represents a table in a relational database, and each entity instance corresponds to a row in that table.
public class CategoricalVars extends StatsFunctions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String explanatoryName;
    private String responseName;

    @Lob
    private double[][] frequencies;

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

    // Override toString method to handle the array printing properly
    @Override
    public String toString() {
        return "CategoricalVars{" +
                "id=" + id +
                ", explanatoryName='" + explanatoryName + '\'' +
                ", responseName='" + responseName + '\'' +
                ", frequencies=" + Arrays.deepToString(frequencies) +
                '}';
    }
}
