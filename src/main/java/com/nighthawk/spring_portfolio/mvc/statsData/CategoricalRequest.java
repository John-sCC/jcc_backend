package com.nighthawk.spring_portfolio.mvc.statsData;

import java.util.ArrayList;

public class CategoricalRequest {
    private int size;
    private ArrayList<String> data;

    // Getters and Setters
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}
