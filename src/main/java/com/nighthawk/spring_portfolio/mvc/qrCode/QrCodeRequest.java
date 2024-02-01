package com.nighthawk.spring_portfolio.mvc.qrCode;

import java.util.List;

import lombok.Data;

@Data
public class QrCodeRequest {
    private List<String> links;
    private List<Double> frequencies; // Corrected spelling

    // Constructors, getters, and setters
}

