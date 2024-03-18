package com.nighthawk.spring_portfolio.mvc.statsData;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController // annotation to simplify the creation of RESTful web services
@RequestMapping("/api/stats")
public class StatsApiController {
<<<<<<< HEAD
=======
    /*
>>>>>>> 342a2a33bdecc333f9b0f8281156f5289dda5d8a
    @GetMapping("/get")
    public ResponseEntity<List<double>> getCorrelation() {
        List<Quantitative> quan = repository.findAll();
        List<double> correlations = new ArrayList<>();
        for (int i=0;i<quan.size();i++){
            correlations.add(quan.get(i).getCorrelation());
        } 
        return correlations;
    }
<<<<<<< HEAD
    
=======
    */
>>>>>>> 342a2a33bdecc333f9b0f8281156f5289dda5d8a
}
