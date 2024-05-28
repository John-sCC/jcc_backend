package com.nighthawk.spring_portfolio.mvc.qrCode;

import java.util.List;

import lombok.Data;

@Data
public class QrCodeRequest {
    private String name;
    private List<String> links;
    private List<Double> frequencies; 
}

