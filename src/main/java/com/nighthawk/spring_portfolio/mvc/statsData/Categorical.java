package com.nighthawk.spring_portfolio.mvc.statsData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity // Annotation to simplify creating an entity, which is a lightweight persistence domain object. Typically, an entity represents a table in a relational database, and each entity instance corresponds to a row in that table.
public class Categorical extends StatsFunctions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int size;
    
    private List<String> variableNames; //if size == 2, first val = explanatory, second = response

    @Convert(converter = MapToStringConverter.class)
    private Map<String, Double> items = new HashMap<>(); // Name, Frequency

    // Method to calculate relative frequency
    public Map<String, Double> calculateRelativeFrequency() {
        double total = items.values().stream().mapToDouble(Double::doubleValue).sum();
        return items.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue() / total
            ));
    }

    // Method to generate a bar chart
    public void generateBarChart(String chartTitle, String categoryAxisLabel, String valueAxisLabel, String filePath) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        items.forEach((key, value) -> {
            dataset.addValue(value, "Frequency", key);
        });
        
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
}
