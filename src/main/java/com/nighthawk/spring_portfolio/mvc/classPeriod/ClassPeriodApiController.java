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
import com.nighthawk.spring_portfolio.mvc.person.PersonDetailsService;
import com.fasterxml.jackson.databind.JsonNode;
import com.nighthawk.spring_portfolio.mvc.jwt.JwtTokenUtil;

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

    @Autowired
    private PersonDetailsService personService;

    @Autowired
    private JwtTokenUtil tokenUtil;

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
    public ResponseEntity<Object> postClassPeriod(@CookieValue("jwt") String jwtToken,
                                                  @RequestBody ClassPeriodRequest request) {
        // checking if JWT token is missing
        if (jwtToken.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // getting user data
        String userEmail = tokenUtil.getUsernameFromToken(jwtToken);
        Person newLeader = personRepository.findByEmail(userEmail);
        if (newLeader != null) {
            ClassPeriod classPeriod = new ClassPeriod(request.getName());
            classPeriodDetailsService.save(classPeriod);
            classPeriodDetailsService.addLeaderToClass(newLeader.getEmail(), classPeriod.getId());
            // add other leaders to class
            for (long id : request.getLeaderIds()) {
                classPeriodDetailsService.addLeaderToClass(personService.get(id).getEmail(), classPeriod.getId());
            }
            // add other students to class
            for (long id : request.getStudentIds()) {
                classPeriodDetailsService.addStudentToClass(personService.get(id).getEmail(), classPeriod.getId());
            }
            return new ResponseEntity<>(classPeriod, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("The user set to become the class owner couldn't be found. The login token may have expired.", HttpStatus.NOT_FOUND);
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

    @GetMapping("/dashboard")
    public ResponseEntity<?> fetchBothClassData(@CookieValue("jwt") String jwtToken) {
        // checking if JWT token is missing
        if (jwtToken.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // getting user data
        String userEmail = tokenUtil.getUsernameFromToken(jwtToken);
        Person existingPerson = personRepository.findByEmail(userEmail);
        if (existingPerson == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<ClassPeriod> student = classPeriodDetailsService.getClassPeriodsByStudent(existingPerson);
        List<ClassPeriod> leader = classPeriodDetailsService.getClassPeriodsByLeader(existingPerson);

        // initializing storage device vrrrmmmm ERRT ERRT ERRT beeeeeep
        HashMap<String, List<ClassPeriod>> classData = new HashMap<>();

        // adding class periods to storage device brrp brrp bleeeeeeebpt
        classData.put("student", student);
        classData.put("leader", leader);

        // return class data
        return new ResponseEntity<>(classData, HttpStatus.OK);
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
    public ResponseEntity<Object> setSeatingChart(@CookieValue("jwt") String jwtToken,
                                                  @RequestBody SeatingChart seatingChart) {
        // checking if JWT token is missing
        if (jwtToken.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // getting user data
        String userEmail = tokenUtil.getUsernameFromToken(jwtToken);
        // find ID 
        ClassPeriod classPeriod = repository.findById((seatingChart.getClassId()));
        if (classPeriod != null) {  // Good ID
            boolean userIsLeader = false;
            for (Person leader : classPeriod.getLeaders()) {
                if (leader.getEmail().equals(userEmail)) {
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

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateClassPeriod(@PathVariable long id, @RequestBody JsonNode requestBody) {
        ClassPeriod classPeriod = repository.findById(id);

        // Extract name from the request body
        String name = requestBody.get("name").asText();

        // Extract students from the request body and convert them to an ArrayList<String>
        JsonNode studentsNode = requestBody.get("class");
        ArrayList<String> students = new ArrayList<>();
        if (studentsNode.isArray()) {
            for (JsonNode studentNode : studentsNode) {
                students.add(studentNode.asText());
            }
        }

        // Set the properties of the existing class period
        classPeriod.setName(name);
        classPeriod.setStudents(students);

        // Print for verification
        System.out.println("Name: " + name);
        System.out.println("Students: " + students);

        // Save the updated class period to the database
        repository.save(classPeriod);

        // Return an appropriate response
        return new ResponseEntity<>("Class period updated successfully", HttpStatus.OK);
    }
}

