package com.nighthawk.spring_portfolio.mvc.statsData;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nighthawk.spring_portfolio.mvc.jwt.JwtTokenUtil;
import com.nighthawk.spring_portfolio.mvc.qrCode.QrCodeDetailsService;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private CategoricalVarsJpaRepository cVarsRepository;
    @Autowired
    private TwoCategoricalJpaRepository twoCategoricalRepository;

    @Autowired
    private JwtTokenUtil tokenUtil;

    @Autowired
    private QuantitativeDetailsService quantitativeDetailsService;

    @Autowired
    private TwoQuantitativeDetailsService twoQuantitativeDetailsService;


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
    public ResponseEntity<Quantitative> newQuantitative(@CookieValue("jwt") String jwtToken, @RequestBody QuantitativeRequest quantitativeRequest) {
        List<Double> data = quantitativeRequest.getData();
        String name = quantitativeRequest.getName(); 

        String userEmail = tokenUtil.getUsernameFromToken(jwtToken);

        Quantitative quantitative = new Quantitative(data, name);
                
        qRepository.save(quantitative);

        quantitativeDetailsService.addQuantitativeToUser(userEmail, quantitative.getId());

        return new ResponseEntity<>(quantitative, HttpStatus.OK);
    }

    @PostMapping("/newTwoQuantitative")
    public ResponseEntity<TwoQuantitative> newCode(@CookieValue("jwt") String jwtToken, @RequestBody TwoQuantitativeRequest twoQuantitativeRequest) {
        List<Double> explanatory = twoQuantitativeRequest.getExplanatory();
        List<Double> response = twoQuantitativeRequest.getResponse();
        String explanatoryName = twoQuantitativeRequest.getExplanatoryName(); 
        String responseName = twoQuantitativeRequest.getResponseName(); 

        Quantitative quantitative1 = new Quantitative(explanatory, explanatoryName);
        Quantitative quantitative2 = new Quantitative(response, responseName);
                
        qRepository.save(quantitative1);
        qRepository.save(quantitative2);

        String userEmail = tokenUtil.getUsernameFromToken(jwtToken);

        List<List<Double>> inputs = new ArrayList<>();

        inputs.add(explanatory);
        inputs.add(response);

        TwoQuantitative twoQuantitative = new TwoQuantitative(inputs, quantitative1.getId(), quantitative2.getId());

        twoQRepository.save(twoQuantitative);

        twoQuantitativeDetailsService.addTwoQuantitativeToUser(userEmail, twoQuantitative.getId());

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

    @GetMapping("/categorical")
    public ResponseEntity<List<Categorical>> getCategoricals() {
        List<Categorical> categoricals = cRepository.findAll();
        return new ResponseEntity<>(categoricals, HttpStatus.OK);
    }

    @PostMapping("/newCategorical")
    public ResponseEntity<Categorical> newCategorical(@RequestBody CategoricalRequest categoricalRequest) {
        int size = categoricalRequest.getSize();
        Map<String, Double> data = categoricalRequest.getData();

        Categorical categorical = new Categorical();
        categorical.setSize(size);
        categorical.setItems(data);

        cRepository.save(categorical);

        return new ResponseEntity<>(categorical, HttpStatus.OK);
    }

    @GetMapping("/getCategorical{id}")
    public ResponseEntity<Categorical> getCategorical(@PathVariable long id) {
        Optional<Categorical> optional = cRepository.findById(id);
        if (optional.isPresent()) {  // Good ID
            Categorical categorical = optional.get();  // value from findByID
            return new ResponseEntity<>(categorical, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);       
    }

    @GetMapping("/categoricalVars")
    public ResponseEntity<List<CategoricalVars>> getCategoricalVars() {
        List<CategoricalVars> categoricalVarsList = cVarsRepository.findAll();
        return new ResponseEntity<>(categoricalVarsList, HttpStatus.OK);
    }

    @PostMapping("/newCategoricalVars")
    public ResponseEntity<CategoricalVars> newCategoricalVars(@RequestBody CategoricalVarsRequest categoricalVarsRequest) {
        String explanatoryName = categoricalVarsRequest.getExplanatoryName();
        String responseName = categoricalVarsRequest.getResponseName();
        double[][] frequencies = categoricalVarsRequest.getFrequencies();

        CategoricalVars categoricalVars = new CategoricalVars();
        categoricalVars.setExplanatoryName(explanatoryName);
        categoricalVars.setResponseName(responseName);
        categoricalVars.setFrequencies(frequencies);

        cVarsRepository.save(categoricalVars);

        return new ResponseEntity<>(categoricalVars, HttpStatus.OK);
    }

    @GetMapping("/getCategoricalVars{id}")
    public ResponseEntity<CategoricalVars> getCategoricalVars(@PathVariable long id) {
        Optional<CategoricalVars> optional = cVarsRepository.findById(id);
        if (optional.isPresent()) {
            CategoricalVars categoricalVars = optional.get();
            return new ResponseEntity<>(categoricalVars, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/twoCategorical")
    public ResponseEntity<List<TwoCategorical>> getTwoCategoricals() {
        List<TwoCategorical> twoCategoricals = twoCategoricalRepository.findAll();
        return new ResponseEntity<>(twoCategoricals, HttpStatus.OK);
    }

    @PostMapping("/newTwoCategorical")
    public ResponseEntity<TwoCategorical> newTwoCategorical(@RequestBody TwoCategoricalRequest twoCategoricalRequest) {
        String explanatoryName = twoCategoricalRequest.getExplanatoryName();
        String responseName = twoCategoricalRequest.getResponseName();
        double[][] frequencies = twoCategoricalRequest.getFrequencies();

        TwoCategorical twoCategorical = new TwoCategorical();
        twoCategorical.setExplanatoryName(explanatoryName);
        twoCategorical.setResponseName(responseName);
        twoCategorical.setFrequencies(frequencies);

        twoCategoricalRepository.save(twoCategorical);

        return new ResponseEntity<>(twoCategorical, HttpStatus.OK);
    }

    @GetMapping("/getTwoCategorical{id}")
    public ResponseEntity<TwoCategorical> getTwoCategorical(@PathVariable long id) {
        Optional<TwoCategorical> optional = twoCategoricalRepository.findById(id);
        if (optional.isPresent()) {
            TwoCategorical twoCategorical = optional.get();
            return new ResponseEntity<>(twoCategorical, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
