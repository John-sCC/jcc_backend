package com.nighthawk.spring_portfolio.mvc.classPeriod;

import java.util.Map;

/*
 * {
  "chart": {
    "1": {
      "1": "John",
      "2": "Alice"
    },
    "2": {
      "1": "Bob",
      "2": "Eve"
    }
  },
  "classId": 123
}
 */

public class SeatingChart {
    private Map<Integer,Map<Integer, String>> chart;
    private long classId;

    public SeatingChart(Map<Integer,Map<Integer, String>> chart, long classId) {
        this.chart = chart;
        this.classId = classId;
    }

    public Map<Integer,Map<Integer, String>> getChart() {
        return this.chart;
    }

    public long getClassId() {
        return this.classId;
    }

    public void setChart(Map<Integer,Map<Integer, String>> chart) {
        this.chart = chart;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }
}
