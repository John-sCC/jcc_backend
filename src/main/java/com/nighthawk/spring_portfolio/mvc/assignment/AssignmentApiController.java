package com.nighthawk.spring_portfolio.mvc.assignment;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nighthawk.spring_portfolio.mvc.person.PersonDetailsService;
import com.nighthawk.spring_portfolio.mvc.person.PersonJpaRepository;
import com.nighthawk.spring_portfolio.mvc.person.Person;



import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/assignment")
public class AssignmentApiController {
    //     @Autowired
    // private JwtTokenUtil jwtGen;
    /*
    #### RESTful API ####
    Resource: https://spring.io/guides/gs/rest-service/
    */

    // Autowired enables Control to connect POJO Object through JPA
    @Autowired
    private AssignmentJpaRepository repository;

    @Autowired
    private AssignmentDetailsService assignmentDetailsService;

    /*
    GET List of People
     */
    @GetMapping("/")
    public ResponseEntity<List<Assignment>> getAssignments() {
        return new ResponseEntity<>( repository.findAllByOrderByNameAsc(), HttpStatus.OK);
    }

    /*
    GET individual Person using ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignment(@PathVariable long id) {
        Optional<Assignment> optional = repository.findById(id);
        if (optional.isPresent()) {  // Good ID
            Assignment assignment = optional.get();  // value from findByID
            return new ResponseEntity<>(assignment, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);       
    }

    /*
    DELETE individual Person using ID
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Assignment> deletePerson(@PathVariable long id) {
        Optional<Assignment> optional = repository.findById(id);
        if (optional.isPresent()) {  // Good ID
            Assignment assignment = optional.get();  // value from findByID
            repository.deleteById(id);  // value from findByID
            return new ResponseEntity<>(assignment, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
    }

    /*
    POST Aa record by Requesting Parameters from URI
     */
    @PostMapping( "/post")
    public ResponseEntity<Object> postAssignment(@RequestParam("name") String name,
                                                 @RequestParam("dateCreated") String dateCreatedString,
                                                 @RequestParam("dateDue") String dateDueString,
                                                 @RequestParam("content") String content) {
        Date dateCreated;
        Date dateDue;
        try {
            dateCreated = new SimpleDateFormat("MM-dd-yyyy").parse(dateCreatedString);
        } catch (Exception e) {
            return new ResponseEntity<>(dateCreatedString +" error; try MM-dd-yyyy", HttpStatus.BAD_REQUEST);
        }
        try {
            dateDue = new SimpleDateFormat("MM-dd-yyyy").parse(dateDueString);
        } catch (Exception e) {
            return new ResponseEntity<>(dateDueString +" error; try MM-dd-yyyy", HttpStatus.BAD_REQUEST);
        }
        // A assignment object WITHOUT ID will create a new record with default roles as student
        Assignment assignment = new Assignment(name, dateCreated, dateDue, content);
        assignmentDetailsService.save(assignment);
        return new ResponseEntity<>(name +" is created successfully", HttpStatus.CREATED);
    }

    /*
    The assignmentSearch API looks across database for partial match to term (k,v) passed by RequestEntity body
     */
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> assignmentSearch(@RequestBody final Map<String,String> map) {
        // extract term from RequestEntity
        String term = (String) map.get("term");

        // JPA query to filter on term
        List<Person> list = repository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(term, term);

        // return resulting list and status, error checking should be added
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /*
    The assignmentStats API adds stats by Date to Person table 
    */
    @PostMapping(value = "/setStats", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> assignmentStats(@RequestBody final Map<String,Object> stat_map) {
        // find ID
        long id=Long.parseLong((String)stat_map.get("id"));  
        Optional<Assignment> optional = repository.findById((id));
        if (optional.isPresent()) {  // Good ID
            Assignment assignment = optional.get();  // value from findByID

            // Extract Attributes from JSON
            Map<String, Object> attributeMap = new HashMap<>();
            for (Map.Entry<String,Object> entry : stat_map.entrySet())  {
                // Add all attribute other thaN "date" to the "attribute_map"
                if (!entry.getKey().equals("date") && !entry.getKey().equals("id"))
                    attributeMap.put(entry.getKey(), entry.getValue());
            }

            // Set Date and Attributes to SQL HashMap
            Map<String, Map<String, Object>> date_map = new HashMap<>();
            date_map.put( (String) stat_map.get("date"), attributeMap );
            assignment.setStats(date_map);  // BUG, needs to be customized to replace if existing or append if new
            repository.save(assignment);  // conclude by writing the stats updates

            // return Person with update Stats
            return new ResponseEntity<>(assignment, HttpStatus.OK);
        }
        // return Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
    }
}
