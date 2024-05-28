package com.nighthawk.spring_portfolio.mvc.statsData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

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
    private ArrayList<String> data;

    // Custom setters if needed, otherwise Lombok will generate them
    public void setSize(int size) {
        this.size = size;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    private ArrayList<String> variableNames; //if size == 2, first val = explanatory, second = response

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
}
