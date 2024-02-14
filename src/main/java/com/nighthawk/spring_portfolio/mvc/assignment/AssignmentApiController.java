package com.nighthawk.spring_portfolio.mvc.assignment;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nighthawk.spring_portfolio.mvc.classPeriod.ClassPeriodDetailsService;

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

    @Autowired
    private ClassPeriodDetailsService classService;

    /*
    GET individual Person using ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignment(@PathVariable long id) {
        Assignment assignment = repository.findById(id);
        if (assignment != null) {  // Good ID
            return new ResponseEntity<>(assignment, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);       
    }

    /*
    DELETE individual Person using ID
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Assignment> deleteAssignment(@PathVariable long id) {
        Assignment assignment = repository.findById(id);
        if (assignment != null) {  // Good ID
            repository.deleteById(id);  // value from findByID
            return new ResponseEntity<>(assignment, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
    }

    /*
    POST Aa record by Requesting Parameters from URI
     */
    @PostMapping("/post")
    public ResponseEntity<Object> postAssignment(@RequestBody AssignmentRequest request) {
        for (String className : request.getClassNames()) {
            if (classService.getByName(className) == null) {
                return new ResponseEntity<>("One or more classes was invalid", HttpStatus.BAD_REQUEST);
            }
        }
        // A assignment object WITHOUT ID will create a new record with default roles as student
        Assignment assignment = new Assignment(request.getName(), request.getDateCreated(), request.getDateDue(), request.getContent());
        assignmentDetailsService.save(assignment);
        for (String className : request.getClassNames()) {
            classService.addAssignmentToClass(assignment.getId(), className);
        }
        return new ResponseEntity<>(assignment.getName() +" is created successfully", HttpStatus.CREATED);
    }

    /*
    The assignmentSearch API looks across database for partial match to term (k,v) passed by RequestEntity body
     */
    // @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<Object> assignmentSearch(@RequestBody final Map<String,String> map) {
    //     // extract term from RequestEntity
    //     String term = (String) map.get("term");

    //     // JPA query to filter on term
    //     List<Assignment> list = repository.findByName(term);

    //     // return resulting list and status, error checking should be added
    //     return new ResponseEntity<>(list, HttpStatus.OK);
    // }
}
