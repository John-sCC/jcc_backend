package com.nighthawk.spring_portfolio.mvc.assignment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
This class has an instance of Java Persistence API (JPA)
-- @Autowired annotation. Allows Spring to resolve and inject collaborating beans into our bean.
-- Spring Data JPA will generate a proxy instance
-- Below are some CRUD methods that we can use with our database
*/
@Service
@Transactional
public class AssignmentDetailsService implements UserDetailsService {
    // Encapsulate many object into a single Bean (Person, Roles, and Scrum)
    @Autowired  // Inject PersonJpaRepository
    private AssignmentJpaRepository assignmentJpaRepository;

    /* UserDetailsService Overrides and maps Person & Roles POJO into Spring Security */
    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Person person = personJpaRepository.findByEmail(email); // setting variable user equal to the method finding the username in the database
        // if(person==null) {
		// 	throw new UsernameNotFoundException("User not found with username: " + email);
        // }
        // Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // person.getRoles().forEach(role -> { //loop through roles
        //     authorities.add(new SimpleGrantedAuthority(role.getName())); //create a SimpleGrantedAuthority by passed in role, adding it all to the authorities list, list of roles gets past in for spring security
        // });
        // // train spring security to User and Authorities
        // return new org.springframework.security.core.userdetails.User(person.getEmail(), person.getPassword(), authorities);
        return null;
    }

    /* Assignment Section */
    
    // custom query to find match to name or email
    /*
    public  List<Person>list(String name, arraylist classes) {
        return assignmentJpaRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(name, classes);
    }
    */

    // custom query to find anything containing term in name or email ignoring case
    /*
    public  List<Person>list(Date dateCreated, Date dateDue) {
        return assignmentJpaRepository.findByDateCreatedAndDateDue(dateCreated, dateDue);
    }
    */

    public Assignment get(long id) {
        return (assignmentJpaRepository.findById(id) != null)
                ? assignmentJpaRepository.findById(id)
                : null;
    }

    public List<Assignment> findByName(String name) {
        return (assignmentJpaRepository.findByName(name));
    }

    public Assignment getBySubmission(AssignmentSubmission submission) {
        return (assignmentJpaRepository.findBySubmissionsContaining(submission));
    }

    public void delete(long id) {
        assignmentJpaRepository.deleteById(id);
    }

    public void save(Assignment assignment) {
        assignmentJpaRepository.save(assignment);
    }

    // submission methods
    public void addSubmissionToAssignment(Assignment assignment, AssignmentSubmission assignmentSubmission) {
        // prior processing is expected to be done with the endpoint method
        assignment.getSubmissions().add(assignmentSubmission);
    }
}
