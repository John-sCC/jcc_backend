package com.nighthawk.spring_portfolio.mvc.statsData;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nighthawk.spring_portfolio.mvc.qrCode.LinkFreq;
import com.nighthawk.spring_portfolio.mvc.qrCode.QrCode;
import com.nighthawk.spring_portfolio.mvc.qrCode.QrCodeRequest;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController // annotation to simplify the creation of RESTful web services
@RequestMapping("/api/stats")  // all requests in file begin with this URI
public class StatsApiController {

    // Autowired enables Control to connect URI request and POJO Object to easily for Database CRUD operations
    @Autowired
    private QuantitativeJpaRepository qRepository;

    @Autowired
    private TwoQuantitativeJpaRepository twoQRepository;

    @Autowired
    private CategoricalJpaRepository cRepository;

    /* GET List of all of any type of data
     * @GetMapping annotation is used for mapping HTTP GET requests onto specific handler methods.
     */

    @GetMapping("/twoQuantitative")
    @Transactional
    public ResponseEntity<List<TwoQuantitative>> getTwoQuantitatives() {
        List<TwoQuantitative> twoQuantitatives = twoQRepository.findAll();
        twoQuantitatives.forEach(quantitative -> Hibernate.initialize(quantitative.getLsrl()));
        return new ResponseEntity<>(twoQuantitatives, HttpStatus.OK);
    }   

    @GetMapping("/quantitative")
    public ResponseEntity<List<Quantitative>> getQuantitatives() {
        List<Quantitative> quantitative = qRepository.findAll();
        return new ResponseEntity<>(quantitative, HttpStatus.OK);
    }   

    @PostMapping("/newQuantitative")
    public ResponseEntity<Quantitative> newQuantitative(@RequestBody QuantitativeRequest quantitativeRequest) {
        List<Double> data = quantitativeRequest.getData();
        String name = quantitativeRequest.getName(); 

        Quantitative quantitative = new Quantitative(data, name);
                
        qRepository.save(quantitative);

        return new ResponseEntity<>(quantitative, HttpStatus.OK);
    }

    @PostMapping("/newTwoQuantitative")
    public ResponseEntity<TwoQuantitative> newCode(@RequestBody TwoQuantitativeRequest twoQuantitativeRequest) {
        List<Double> explanatory = twoQuantitativeRequest.getExplanatory();
        List<Double> response = twoQuantitativeRequest.getResponse();
        String explanatoryName = twoQuantitativeRequest.getExplanatoryName(); 
        String responseName = twoQuantitativeRequest.getResponseName(); 

        Quantitative quantitative1 = new Quantitative(explanatory, explanatoryName);
        Quantitative quantitative2 = new Quantitative(response, responseName);
                
        qRepository.save(quantitative1);
        qRepository.save(quantitative2);

        List<List<Double>> inputs = new ArrayList<>();

        inputs.add(explanatory);
        inputs.add(response);

        TwoQuantitative twoQuantitative = new TwoQuantitative(inputs, quantitative1.getId(), quantitative2.getId());

        twoQRepository.save(twoQuantitative);

        return new ResponseEntity<>(twoQuantitative, HttpStatus.OK);
    }    

    /* GET Specific of any type of data 
     * @GetMapping annotation is used for mapping HTTP GET requests onto specific handler methods.
     */
    @GetMapping("/getQuantitative{id}")
    public ResponseEntity<Quantitative> getQuantitative(@PathVariable long id) {
        Optional<Quantitative> optional = qRepository.findById(id);
        if (optional.isPresent()) {  // Good ID
            Quantitative quantitative = optional.get();  // value from findByID
            return new ResponseEntity<>(quantitative, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);       
    }

    @GetMapping("/getTwoQuantitative{id}")
    @Transactional
    public ResponseEntity<TwoQuantitative> getTwoQuantitative(@PathVariable long id) {
        Optional<TwoQuantitative> optional = twoQRepository.findById(id);
        if (optional.isPresent()) {  // Good ID
            TwoQuantitative twoQuantitative = optional.get();  // value from findByID
            Hibernate.initialize(twoQuantitative.getLsrl());
            return new ResponseEntity<>(twoQuantitative, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);       
    }

    // @GetMapping("/getCategorical{id}")
    // public ResponseEntity<Categorical> getCategorical(@PathVariable long id) {
    //     Optional<Categorical> optional = cRepository.findById(id);
    //     if (optional.isPresent()) {  // Good ID
    //         Categorical categorical = optional.get();  // value from findByID
    //         return new ResponseEntity<>(categorical, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
    //     }
    //     // Bad ID
    //     return new ResponseEntity<>(HttpStatus.BAD_REQUEST);       
    // }

    // @GetMapping("/getTwoCategorical{id}")
    // @Transactional
    // public ResponseEntity<TwoCategorical> getTwoCategorical(@PathVariable long id) {
    //     Optional<TwoCategorical> optional = twoCRepository.findById(id);
    //     if (optional.isPresent()) {  // Good ID
    //         TwoCategorical twoCategorical = optional.get();  // value from findByID
    //         Hibernate.initialize(twoCategorical.getLsrl());
    //         return new ResponseEntity<>(twoCategorical, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
    //     }
    //     // Bad ID
    //     return new ResponseEntity<>(HttpStatus.BAD_REQUEST);       
    // }



}
