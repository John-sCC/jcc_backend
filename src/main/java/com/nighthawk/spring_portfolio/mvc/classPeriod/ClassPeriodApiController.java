package com.nighthawk.spring_portfolio.mvc.classPeriod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Object> postClassPeriod(@RequestParam("email") String email,
                                             @RequestParam("name") String name) {
        // A person object WITHOUT ID will create a new record with default roles as student
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
    @PostMapping(value = "/set_seating_chart", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClassPeriod> personStats(@RequestParam("chart") Map<Integer,Map<Integer, String>> seat_map,
                                                   @RequestParam("class_id") long class_id,
                                                   @RequestParam("") String class_password) {
        // find ID 
        Optional<ClassPeriod> optional = repository.findById((class_id));
        if (optional.isPresent()) {  // Good ID
            ClassPeriod classPeriod = optional.get();  // value from findByID
            classPeriod.setSeatingChart(seat_map);  // BUG, needs to be customized to replace if existing or append if new
            repository.save(classPeriod);  // conclude by writing the stats updates

            // return Person with update Stats
            return new ResponseEntity<>(classPeriod, HttpStatus.OK);
        }
        // return Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
    }
    */
}
