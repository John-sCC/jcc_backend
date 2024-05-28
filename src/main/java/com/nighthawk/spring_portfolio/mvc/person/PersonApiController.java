package com.nighthawk.spring_portfolio.mvc.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nighthawk.spring_portfolio.mvc.jwt.JwtTokenUtil;
import com.nighthawk.spring_portfolio.mvc.statsData.*;

import java.util.*;

@RestController
@RequestMapping("/api/person")
public class PersonApiController {
    // @Autowired
    // private JwtTokenUtil jwtGen;
    /*
     * #### RESTful API ####
     * Resource: https://spring.io/guides/gs/rest-service/
     */

    // Autowired enables Control to connect POJO Object through JPA
    @Autowired
    private PersonJpaRepository repository;

    @Autowired
    private PersonDetailsService personDetailsService;

    @Autowired
    private JwtTokenUtil tokenUtil;

    /*
     * GET List of People
     */
    @GetMapping("/")
    public ResponseEntity<List<Person>> getPeople() {
        return new ResponseEntity<>(repository.findAllByOrderByNameAsc(), HttpStatus.OK);
    }

    /*
     * GET individual Person using ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable long id) {
        Optional<Person> optional = repository.findById(id);
        if (optional.isPresent()) { // Good ID
            Person person = optional.get(); // value from findByID
            return new ResponseEntity<>(person, HttpStatus.OK); // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /*
     * DELETE individual Person using ID
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Person> deletePerson(@PathVariable long id) {
        Optional<Person> optional = repository.findById(id);
        if (optional.isPresent()) { // Good ID
            Person person = optional.get(); // value from findByID
            repository.deleteById(id); // value from findByID
            return new ResponseEntity<>(person, HttpStatus.OK); // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /*
     * NEW METHOD: DELETE self using body
     */
    @DeleteMapping("/delete/self")
    public ResponseEntity<Person> deleteSelf(@RequestBody Person self) {
        Person deletedPerson = repository.findByEmailAndPassword(self.getEmail(), self.getPassword());
        if (deletedPerson != null) { // Good ID
            repository.deleteById(deletedPerson.getId()); // value from findByID
            return new ResponseEntity<>(deletedPerson, HttpStatus.OK); // OK HTTP response: status code, headers, and
                                                                       // body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /*
     * POST Aa record by Requesting Parameters from URI
     */
    @PostMapping("/post")
    public ResponseEntity<Object> postPerson(@RequestBody PersonRequest personRequest) {
        // A person object WITHOUT ID will create a new record with default roles as
        // student
        Person person = new Person(personRequest.getEmail(), personRequest.getPassword(), personRequest.getName(),
                personRequest.getUsn(), personRequest.getSubjectsOfInterest());
        personDetailsService.save(person);
        return new ResponseEntity<>(personRequest.getEmail() + " is created successfully", HttpStatus.CREATED);
    }

    // get persons by subject of interest - endpointmethod
    @GetMapping("/getBySubject/{subjectOfInterest}")
    public ResponseEntity<?> getPersonsBySubject(@PathVariable String subjectOfInterest) {
        List<Person> personList = personDetailsService.getPersonsBySubjectOfInterest(subjectOfInterest);
        // regardless of outcome, even if it's an empty list, it's still a valid output
        return new ResponseEntity<>(personList, HttpStatus.OK);
    }

    @GetMapping("/quantitatives")
    public ResponseEntity<Collection<Quantitative>> getQuantitative(@CookieValue("jwt") String jwtToken){
        String userEmail = tokenUtil.getUsernameFromToken(jwtToken);

        return new ResponseEntity<>(repository.findByEmail(userEmail).getQuantitatives(), HttpStatus.OK);
    }

    @GetMapping("/twoQuantitatives")
    public ResponseEntity<Collection<TwoQuantitative>> getTwoQuantitative(@CookieValue("jwt") String jwtToken){
        String userEmail = tokenUtil.getUsernameFromToken(jwtToken);

        return new ResponseEntity<>(repository.findByEmail(userEmail).getTwoQuantitatives(), HttpStatus.OK);
    }


    /*
     * The personSearch API looks across database for partial match to term (k,v)
     * passed by RequestEntity body
     */
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> personSearch(@RequestBody final Map<String, String> map) {
        // extract term from RequestEntity
        String term = (String) map.get("term");

        // JPA query to filter on term
        List<Person> list = repository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(term, term);

        // return resulting list and status, error checking should be added
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /*
     * NO LONGER NEEDED
     * The personStats API adds stats by Date to Person table
     * 
     * @PostMapping(value = "/setStats", produces =
     * MediaType.APPLICATION_JSON_VALUE)
     * public ResponseEntity<Person> personStats(@RequestBody final
     * Map<String,Object> stat_map) {
     * // find ID
     * long id=Long.parseLong((String)stat_map.get("id"));
     * Optional<Person> optional = repository.findById((id));
     * if (optional.isPresent()) { // Good ID
     * Person person = optional.get(); // value from findByID
     * 
     * // Extract Attributes from JSON
     * Map<String, Object> attributeMap = new HashMap<>();
     * for (Map.Entry<String,Object> entry : stat_map.entrySet()) {
     * // Add all attribute other thaN "date" to the "attribute_map"
     * if (!entry.getKey().equals("date") && !entry.getKey().equals("id"))
     * attributeMap.put(entry.getKey(), entry.getValue());
     * }
     * 
     * // Set Date and Attributes to SQL HashMap
     * Map<String, Map<String, Object>> date_map = new HashMap<>();
     * date_map.put( (String) stat_map.get("date"), attributeMap );
     * repository.save(person); // conclude by writing the stats updates
     * 
     * // return Person with update Stats
     * return new ResponseEntity<>(person, HttpStatus.OK);
     * }
     * // return Bad ID
     * return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
     * }
     */
}
