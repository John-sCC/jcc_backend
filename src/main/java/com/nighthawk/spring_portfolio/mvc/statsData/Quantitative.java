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

    private String name;
    private List<Double> data;
    private double mean;
    private int size;
    private double stDev;
    private double min;
    private double max;
    private double median;
    private double qOne;
    private double qThree;  

    public Quantitative(List<Double> data, String name) {
        this.data = data;
        this.size = data.size();
        this.name = name;
        this.mean = calculateMean(data);
        this.stDev = calculateSD(data);
        this.min = getMinimum(data);
        this.max = getMaximum(data);
        this.median = getMedian(data);
        this.qOne = getQuartileOne(data);
        this.qThree = getQuartileThree(data);
    }

    public void printData(){
        System.out.println("Data " + this.data + " Size " + this.size + " Name " + this.name + " Mean " + this.mean + " Standard Deviation " + this.stDev + " Min/Max " + this.min + "/" + this.max + " Median " + this.median + " qOne " + this.qOne + " qThree " + this.qThree);
    }

    public static Quantitative init() {
        List<Double> dataList1 = Arrays.asList(1.0, 2.0, 3.0, 5.0, 2.0);

        Quantitative quan = new Quantitative(dataList1, "test");

        quan.printData();

        return quan;
    }

    public static void main(String[] args) {
        Quantitative quan = init();

    }
}