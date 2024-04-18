package com.nighthawk.spring_portfolio.mvc.classPeriod;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.nighthawk.spring_portfolio.mvc.person.Person;
import com.nighthawk.spring_portfolio.mvc.assignment.Assignment;

/*
Extends the JpaRepository interface from Spring Data JPA.
-- Java Persistent API (JPA) - Hibernate: map, store, update and retrieve database
-- JpaRepository defines standard CRUD methods
-- Via JPA the developer can retrieve database from relational databases to Java objects and vice versa.
 */
public interface ClassPeriodJpaRepository extends JpaRepository<ClassPeriod, Long> {
    ClassPeriod findById(long id);

    ClassPeriod findByName(String name);

    List<ClassPeriod> findAllByOrderByNameAsc();

    // JPA query, findBy does JPA magic with "Name", "Containing", "Or", "Email", "IgnoreCase"
    List<ClassPeriod> findByNameContainingIgnoreCase(String name);

    List<ClassPeriod> findByLeadersContaining(Person leader);
    List<ClassPeriod> findByStudentsContaining(Person student);
    List<ClassPeriod> findByAssignmentsContaining(Assignment assignment);
}