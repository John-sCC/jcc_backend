package com.nighthawk.spring_portfolio.mvc.assignment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Date;

import com.nighthawk.spring_portfolio.mvc.person.Person;

public interface AssignmentSubmissionJpaRepository extends JpaRepository<AssignmentSubmission, Long> {

    AssignmentSubmission findById(long id);

    List<AssignmentSubmission> findBySubmitter(Person submitter);

    // JPA query, findBy does JPA magic with "Name", "Containing", "Or", "Email", "IgnoreCase"
    //List<Assignment> findByNameContainingIgnoreCaseOrClassesContainingIgnoreCase(String name, arraylist classes);

    /* Custom JPA query articles, there are articles that show custom SQL as well
       https://springframework.guru/spring-data-jpa-query/
       https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
    */
    List<AssignmentSubmission> findBySubmissionNumber(int submissionNumber);

    /*
      https://www.baeldung.com/spring-data-jpa-query
    */
}
