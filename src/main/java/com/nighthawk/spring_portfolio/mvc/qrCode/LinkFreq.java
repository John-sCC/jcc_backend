package com.nighthawk.spring_portfolio.mvc.qrCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

import jakarta.persistence.*;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class LinkFreq {
    
    private String link;
    private double frequency;

}
