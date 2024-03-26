// package com.nighthawk.spring_portfolio.mvc.statsData;

// import org.hibernate.Hibernate;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import jakarta.transaction.Transactional;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;

// @RestController // annotation to simplify the creation of RESTful web services
// @RequestMapping("/api/stats")
// public class StatsApiController {
//     /*
//     @GetMapping("/get")
//     public ResponseEntity<List<double>> getCorrelation() {
//         List<Quantitative> quan = repository.findAll();
//         List<double> correlations = new ArrayList<>();
//         for (int i=0;i<quan.size();i++){
//             correlations.add(quan.get(i).getCorrelation());
//         } 
//         return correlations;
//     }
//     */
//     private final Quantitative quantitativeService;

//     @Autowired
//     public StatsApiController(Quantitative quantitativeService) {
//         this.quantitativeService = quantitativeService;
//     }

//     @PostMapping("/input")
//     public ResponseEntity<Quantitative> addQuantitativeData(@RequestBody Quantitative inputData) {
//         try {
//             Quantitative quantitative = quantitativeService.setInput(inputData);
//             return new ResponseEntity<>(quantitative, HttpStatus.CREATED);
//         } catch (Exception e) {
//             return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//         }
//     }
// }
