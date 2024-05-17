package com.nighthawk.spring_portfolio.mvc.statsData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Set;


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
public class TwoQuantitative extends StatsFunctions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private double correlation;

    private LSRL lsrl;

    private long explanatoryID;

    private long responseID;

    public TwoQuantitative(List<List<Double>> inputs, long explanatoryID, long responseID) {
        this.correlation = calculateCorrelation(inputs.get(0), inputs.get(1));

        this.lsrl = new LSRL(inputs, correlation);

        this.responseID = responseID;
        this.explanatoryID = explanatoryID;

    }

    public void printData(){
        System.out.println("Correlation " + this.getCorrelation());
        System.out.println("Slope " + this.lsrl.getSlope());
        System.out.println("Intercept " + this.lsrl.getIntercept());
        System.out.println("CorrelationSqr " + this.lsrl.getCorrelationSqr());
        System.out.println("Response " + this.getResponseID());
        System.out.println("Explanatory " + this.getExplanatoryID());
    }

    public static void init() {
        List<List<Double>> inputData = new ArrayList<>();
        List<Double> dataList1 = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0);
        List<Double> dataList2 = Arrays.asList(5.0, 3.0, 4.0, 5.0, 6.0);
        inputData.add(dataList1);
        inputData.add(dataList2);

        TwoQuantitative twoQuantitative = new TwoQuantitative(inputData, 1, 2);

        twoQuantitative.printData();
        return;
    }

    public static void main(String[] args) {
        init();
    }
}