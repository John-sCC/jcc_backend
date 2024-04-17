package com.nighthawk.spring_portfolio.mvc.statsData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity // Annotation to simplify creating an entity, which is a lightweight persistence domain object. Typically, an entity represents a table in a relational database, and each entity instance corresponds to a row in that table.
public class Categorical extends StatsFunctions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private List<String> variableNames; //if size == 2, first val = explanatory, second = response
        
    private List<String> category;
    
    private List<Double> frequency;

    public Categorical (List<String> names, List<String> categories, List<Double> frequencies) { //Ok technically names can only have a max size of 2
        this.variableNames = names;
        this.category = category;
        this.frequency = frequency;
    }
    
}
