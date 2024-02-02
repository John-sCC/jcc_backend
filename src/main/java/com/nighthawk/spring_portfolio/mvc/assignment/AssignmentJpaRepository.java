package com.nighthawk.spring_portfolio.mvc.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;


/*
Extends the JpaRepository interface from Spring Data JPA.
-- Java Persistent API (JPA) - Hibernate: map, store, update and retrieve database
-- JpaRepository defines standard CRUD methods
-- Via JPA the developer can retrieve database from relational databases to Java objects and vice versa.
 */
public interface AssignmentJpaRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findAllByOrderByNameAsc();

    List<Assignment> findByName(String name);

    // JPA query, findBy does JPA magic with "Name", "Containing", "Or", "Email", "IgnoreCase"
    //List<Assignment> findByNameContainingIgnoreCaseOrClassesContainingIgnoreCase(String name, arraylist classes);

    /* Custom JPA query articles, there are articles that show custom SQL as well
       https://springframework.guru/spring-data-jpa-query/
       https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
    */
    List<Assignment> findByDateCreatedAndDateDue(Date dateCreated, Date dateDue);

    // Custom JPA query
    @Query(
            value = "SELECT * FROM Person p WHERE p.name LIKE ?1 or p.email LIKE ?1",
            nativeQuery = true)
    List<Assignment> findByLikeTermNative(String term);
    /*
      https://www.baeldung.com/spring-data-jpa-query
    */
}
