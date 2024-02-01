package com.nighthawk.spring_portfolio.mvc.qrCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController // annotation to simplify the creation of RESTful web services
@RequestMapping("/api/QrCode")  // all requests in file begin with this URI
public class QrCodeApiController {

    // Autowired enables Control to connect URI request and POJO Object to easily for Database CRUD operations
    @Autowired
    private QrCodeJpaRepository repository;

    /* GET List of QrCode
     * @GetMapping annotation is used for mapping HTTP GET requests onto specific handler methods.
     */
    @GetMapping("/")
    public ResponseEntity<List<QrCode>> getQrCodes() {
        // ResponseEntity returns List of QrCodes provide by JPA findAll()
        return new ResponseEntity<>( repository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/newCode")
    public ResponseEntity<QrCode> newCode(@RequestBody QrCodeRequest qrCodeRequest) {
        QrCode qrCode = new QrCode();

        List<String> links = qrCodeRequest.getLinks();
        List<Double> frequencys = qrCodeRequest.getFrequencies(); 
        
        for (int i = 0; i < links.size(); i ++){
            qrCode.addLink(new LinkFreq(links.get(i), frequencys.get(i)));
        }
        
        repository.save(qrCode);

        return new ResponseEntity<>(qrCode, HttpStatus.OK);
    }
}
