package com.nighthawk.spring_portfolio.mvc.qrCode;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nighthawk.spring_portfolio.mvc.classPeriod.ClassPeriodDetailsService;
import com.nighthawk.spring_portfolio.mvc.jwt.JwtTokenUtil;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController // annotation to simplify the creation of RESTful web services
@RequestMapping("/api/qrcode")  // all requests in file begin with this URI
public class QrCodeApiController {

    // Autowired enables Control to connect URI request and POJO Object to easily for Database CRUD operations
    @Autowired
    private QrCodeJpaRepository repository;

    @Autowired
    private QrCodeDetailsService qrCodeDetailsService;

    @Autowired
    private JwtTokenUtil tokenUtil;

    /* GET List of QrCode
     * @GetMapping annotation is used for mapping HTTP GET requests onto specific handler methods.
     */

    @GetMapping("/")
    @Transactional
    public ResponseEntity<List<QrCode>> getQrCodes() {
        List<QrCode> qrCodes = repository.findAll();
        qrCodes.forEach(qrCode -> Hibernate.initialize(qrCode.getLinkFreqs()));
        return new ResponseEntity<>(qrCodes, HttpStatus.OK);
    }   

    @PostMapping("/newCode")
    public ResponseEntity<QrCode> newCode(@CookieValue("jwt") String jwtToken, @RequestBody QrCodeRequest qrCodeRequest) {
        String name = qrCodeRequest.getName();
        QrCode qrCode = new QrCode(name);

        String userEmail = tokenUtil.getUsernameFromToken(jwtToken);

        List<String> links = qrCodeRequest.getLinks();
        List<Double> frequencys = qrCodeRequest.getFrequencies(); 
        
        for (int i = 0; i < links.size(); i ++){
            qrCode.addLink(new LinkFreq(links.get(i), frequencys.get(i)));
        }
        
        repository.save(qrCode);

        qrCodeDetailsService.addQrCodeToUser(userEmail, qrCode.getId());

        return new ResponseEntity<>(qrCode, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<QrCode> getQrCode(@PathVariable long id) {
        Optional<QrCode> optional = repository.findById(id);
        if (optional.isPresent()) {  // Good ID
            QrCode qrCode = optional.get();  // value from findByID
            Hibernate.initialize(qrCode.getLinkFreqs());
            return new ResponseEntity<>(qrCode, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);       
    }

}
