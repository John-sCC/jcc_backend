package com.nighthawk.spring_portfolio.mvc.classPeriod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.nighthawk.spring_portfolio.mvc.person.Person;
import com.nighthawk.spring_portfolio.mvc.person.PersonJpaRepository;

import java.util.*;

@RestController
@RequestMapping("/api/class_period")
public class ClassPeriodApiController {
    //     @Autowired
    // private JwtTokenUtil jwtGen;
    /*
    #### RESTful API ####
    Resource: https://spring.io/guides/gs/rest-service/
    */

    // Autowired enables Control to connect POJO Object through JPA
    @Autowired
    private ClassPeriodJpaRepository repository;

    @Autowired
    private ClassPeriodDetailsService classPeriodDetailsService;

    // for looking at people
    @Autowired
    private PersonJpaRepository personRepository;

    /*
    GET List of classes
     */
    @GetMapping("/")
    public ResponseEntity<List<ClassPeriod>> getClassPeriods() {
        return new ResponseEntity<>( repository.findAllByOrderByNameAsc(), HttpStatus.OK);
    }

    /*
    GET individual ClassPeriod using ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClassPeriod> getClassPeriod(@PathVariable long id) {
        ClassPeriod classPeriod = repository.findById(id);
        if (classPeriod != null) {  // Good ID
            return new ResponseEntity<>(classPeriod, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);       
    }

    /*
    DELETE individual ClassPeriod using ID
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ClassPeriod> deleteClassPeriod(@PathVariable long id) {
        ClassPeriod classPeriod = repository.findById(id);
        if (classPeriod != null) {  // Good ID
            repository.deleteById(id);  // value from findByID
            return new ResponseEntity<>(classPeriod, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
    }

    /*
    POST Aa record by Requesting Parameters from URI
     */
    @PostMapping("/post")
    public ResponseEntity<Object> postClassPeriod(@RequestParam("name") String name) {
        // retrieving the current authentication details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        // checking validity of user email
        Person newLeader = personRepository.findByEmail(email);
        if (newLeader != null) {
            ClassPeriod classPeriod = new ClassPeriod(name);
            classPeriodDetailsService.save(classPeriod);
            classPeriodDetailsService.addLeaderToClass(email, name);
            return new ResponseEntity<>("The class \"" + name + "\" was created successfully!", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("The user set to become the class owner couldn't be found.", HttpStatus.NOT_FOUND);
        }
    }

    /*
     * retrieve leader persons
     * NOTE: WILL BE UPDATED LATER TO BE IMPLEMENTED WITH COOKIE CONTENTS
     */
    @GetMapping("/leaders/{id}")
    public ResponseEntity<List<ClassPeriod>> getClassPeriodsByLeader(@PathVariable long id) {
        Optional<Person> optional = personRepository.findById(id);
        if (optional.isPresent()) {  // Good ID
            Person person = optional.get();
            List<ClassPeriod> leaderships = classPeriodDetailsService.getClassPeriodsByLeader(person);
            return new ResponseEntity<>(leaderships, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);       
    }

    /*
     * retrieve student persons
     */
    @GetMapping("/students/{id}")
    public ResponseEntity<List<ClassPeriod>> getClassPeriodsByStudent(@PathVariable long id) {
        Optional<Person> optional = personRepository.findById(id);
        if (optional.isPresent()) {  // Good ID
            Person person = optional.get();
            List<ClassPeriod> studenthoods = classPeriodDetailsService.getClassPeriodsByStudent(person);
            return new ResponseEntity<>(studenthoods, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);       
    }

    // /*
    // The personSearch API looks across database for partial match to term (k,v) passed by RequestEntity body
    //  */
    // @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<Object> personSearch(@RequestBody final Map<String,String> map) {
    //     // extract term from RequestEntity
    //     String term = (String) map.get("term");

    //     // JPA query to filter on term
    //     List<Person> list = repository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(term, term);

    //     // return resulting list and status, error checking should be added
    //     return new ResponseEntity<>(list, HttpStatus.OK);
    // }

    /*
    The this method will be used to add the seating chart to the class
    */
    @PostMapping("/set_seating_chart")
    public ResponseEntity<Object> setSeatingChart(@RequestBody SeatingChart seatingChart) {
        // retrieving the current authentication details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        // find ID 
        ClassPeriod classPeriod = repository.findById((seatingChart.getClassId()));
        if (classPeriod != null) {  // Good ID
            boolean userIsLeader = false;
            for (Person leader : classPeriod.getLeaders()) {
                if (leader.getEmail().equals(email)) {
                    userIsLeader = true;
                }
            }
            if (userIsLeader) {
                classPeriod.setSeatingChart(seatingChart.getChart());  // htis owrks now
                repository.save(classPeriod);  // conclude by writing the stats updates

                // return success
                return new ResponseEntity<>("Seating chart successfully modified.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("The login token does not provide proper authentication to change the seating chart.", HttpStatus.FORBIDDEN);
            }
        }
        // return Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
    }
}
