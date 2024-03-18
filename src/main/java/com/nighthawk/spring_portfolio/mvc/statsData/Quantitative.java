package com.nighthawk.spring_portfolio.mvc.statsData;

<<<<<<< HEAD
=======
/*
>>>>>>> 342a2a33bdecc333f9b0f8281156f5289dda5d8a
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
<<<<<<< HEAD
@Entity // Annotation to simplify creating an entity, which is a lightweight persistence domain object. Typically, an entity represents a table in a relational database, and each entity instance corresponds to a row in that table.
public class Quantitative extends StatsFunctions{
=======
@Entity
public class Quantitative extends StatsFunctions {

>>>>>>> 342a2a33bdecc333f9b0f8281156f5289dda5d8a
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private List<String> variable;
<<<<<<< HEAD
    
    private List<List<Double>> values;
=======

    @Lob
    private byte[] values;
>>>>>>> 342a2a33bdecc333f9b0f8281156f5289dda5d8a

    private double correlation;

    public Quantitative(List<String> variableNames, List<List<Double>> variableValues) {
        if (variableNames.size() != variableValues.size()) {
            throw new IllegalArgumentException("Lists must have the same size");
        }
        this.variable = variableNames;
<<<<<<< HEAD
        this.values = variableValues;
        correlation = calculateCorrelation(variableValues.get(0), variableValues.get(1));
    }

    public double getCorrelation(){
        return this.correlation;
    }

}
=======
        //this.values = serializeValues(variableValues);
        correlation = calculateCorrelation(variableValues.get(0), variableValues.get(1));
    }

    public double getCorrelation() {
        return this.correlation;
    }

    private byte[] serializeValues(List<List<Double>> values) {
        // Serialize the list of lists into byte array (e.g., using JSON serialization)
        // You can use libraries like Jackson ObjectMapper for JSON serialization
        // Convert values to JSON string and then to byte array
        // For simplicity, assuming JSON serialization here
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(values);
            return json.getBytes(StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error serializing values to JSON", e);
        }
    }

    private List<List<Double>> deserializeValues(byte[] serializedValues) {
        // Deserialize the byte array back to list of lists (e.g., using JSON deserialization)
        // You can use libraries like Jackson ObjectMapper for JSON deserialization
        // Convert byte array to JSON string and then deserialize
        // For simplicity, assuming JSON deserialization here
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = new String(serializedValues, StandardCharsets.UTF_8);
            return objectMapper.readValue(json, new TypeReference<List<List<Double>>>() {});
        } catch (IOException e) {
            throw new IllegalArgumentException("Error deserializing values from JSON", e);
        }
    }
}
*/
>>>>>>> 342a2a33bdecc333f9b0f8281156f5289dda5d8a
