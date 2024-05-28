package com.nighthawk.spring_portfolio.mvc.statsData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoricalVarsRequest {
    private String explanatoryName;
    private String responseName;
    private double[][] frequencies;
}
