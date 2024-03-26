package com.nighthawk.spring_portfolio.mvc.statsData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.mongodb.core.schema.JsonSchemaObject.Type.JsonType;

import com.nighthawk.spring_portfolio.mvc.classPeriod.ClassPeriod;

import groovyjarjarantlr4.runtime.misc.Stats;
import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Convert(attributeName ="person", converter = JsonType.class)
public class Quantitative extends StatsFunctions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, List<Double>> input = new HashMap<>(); //variable name, manually input data in a list

    private double correlation;

    public Quantitative(Map<String, List<Double>> input) {
        this.input = input;
        
        correlation = calculateCorrelation(input.get(0), input.get(1));
    }

    public double getCorrelation() {
        return this.correlation;
    }

    public static Quantitative[] init() {
        Map<String, List<Double>> inputData = new HashMap<>();
        List<Double> dataList1 = Arrays.asList(1.0, 2.0, 3.0);
        List<Double> dataList2 = Arrays.asList(4.0, 5.0, 6.0);
        inputData.put("data1", dataList1);
        inputData.put("data2", dataList2);

        Quantitative quan = new Quantitative();
        quan.setInput(inputData);

        Quantitative quantitative[] = {quan};

        return(quantitative);
    }

    public static void main(String[] args) {
        Quantitative quan[] = init();
    }
}