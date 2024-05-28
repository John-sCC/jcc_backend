package com.nighthawk.spring_portfolio.mvc.assignment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nighthawk.spring_portfolio.mvc.person.Person;

/*
This class has an instance of Java Persistence API (JPA)
-- @Autowired annotation. Allows Spring to resolve and inject collaborating beans into our bean.
-- Spring Data JPA will generate a proxy instance
-- Below are some CRUD methods that we can use with our database
*/
@Service
@Transactional
public class AssignmentSubmissionDetailsService {// implements UserDetailsService {
    // Encapsulate many object into a single Bean (Person, Roles, and Scrum)
    @Autowired  // Inject PersonJpaRepository
    private AssignmentSubmissionJpaRepository assignmentJpaRepository;

    public AssignmentSubmission get(long id) {
        return (assignmentJpaRepository.findById(id) != null)
                ? assignmentJpaRepository.findById(id)
                : null;
    }

    public void scoreSubmission(long id, int score) {
        assignmentJpaRepository.findById(id).setScore(score);
    }

    public List<AssignmentSubmission> findBySubmitter(Person submitter) {
        return (assignmentJpaRepository.findBySubmitter(submitter));
    }

    public void delete(long id) {
        assignmentJpaRepository.deleteById(id);
    }

    public void save(AssignmentSubmission assignment) {
        assignmentJpaRepository.save(assignment);
    }
    
}
