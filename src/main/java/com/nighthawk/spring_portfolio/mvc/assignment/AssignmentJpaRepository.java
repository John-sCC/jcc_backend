package com.nighthawk.spring_portfolio.mvc.assignment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Date;


/*
Extends the JpaRepository interface from Spring Data JPA.
-- Java Persistent API (JPA) - Hibernate: map, store, update and retrieve database
-- JpaRepository defines standard CRUD methods
-- Via JPA the developer can retrieve database from relational databases to Java objects and vice versa.
 */
public interface AssignmentJpaRepository extends JpaRepository<Assignment, Long> {

    Assignment findById(long id);

    List<Assignment> findByName(String name);

    // JPA query, findBy does JPA magic with "Name", "Containing", "Or", "Email", "IgnoreCase"
    //List<Assignment> findByNameContainingIgnoreCaseOrClassesContainingIgnoreCase(String name, arraylist classes);

    /* Custom JPA query articles, there are articles that show custom SQL as well
       https://springframework.guru/spring-data-jpa-query/
       https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
    */
    List<Assignment> findByDateDue(Date dateDue);

    // used to find an assignment by its submission, see corresponding details service method
    Assignment findBySubmissionsContaining(AssignmentSubmission submission);

    /*
      https://www.baeldung.com/spring-data-jpa-query
    */
}
