package com.nighthawk.spring_portfolio.mvc.statsData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import groovyjarjarantlr4.runtime.misc.Stats;
import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity // Annotation to simplify creating an entity, which is a lightweight persistence domain object. Typically, an entity represents a table in a relational database, and each entity instance corresponds to a row in that table.
public class Quantitative extends StatsFunctions{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private List<String> variable;
    
    private List<List<Double>> values;

    private double correlation;

    public Quantitative(List<String> variableNames, List<List<Double>> variableValues) {
        if (variableNames.size() != variableValues.size()) {
            throw new IllegalArgumentException("Lists must have the same size");
        }
        this.variable = variableNames;
        this.values = variableValues;
        correlation = calculateCorrelation(variableValues.get(0), variableValues.get(1));
    }

}
