package com.nighthawk.spring_portfolio.mvc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.nighthawk.spring_portfolio.mvc.person.Person;
import com.nighthawk.spring_portfolio.mvc.person.PersonRole;
import com.nighthawk.spring_portfolio.mvc.person.PersonDetailsService;
import com.nighthawk.spring_portfolio.mvc.person.PersonRoleJpaRepository;
import com.nighthawk.spring_portfolio.mvc.classPeriod.ClassPeriodDetailsService;
import com.nighthawk.spring_portfolio.mvc.classPeriod.ClassPeriodJpaRepository;
import com.nighthawk.spring_portfolio.mvc.classPeriod.ClassPeriod;
import com.nighthawk.spring_portfolio.mvc.assignment.AssignmentDetailsService;
import com.nighthawk.spring_portfolio.mvc.assignment.AssignmentJpaRepository;
import com.nighthawk.spring_portfolio.mvc.assignment.Assignment;

import java.util.List;


@Component
@Configuration // Scans Application for ModelInit Bean, this detects CommandLineRunner
public class ModelInit { 
    //declarations 
    @Autowired PersonDetailsService personService;
    @Autowired PersonRoleJpaRepository roleRepo;
    @Autowired ClassPeriodDetailsService classService;
    @Autowired ClassPeriodJpaRepository classRepo;
    @Autowired AssignmentDetailsService assService;
    @Autowired AssignmentJpaRepository assRepo;

    @Bean
    CommandLineRunner run() {  // The run() method will be executed after the application starts
        return args -> {
            // PersonRole databas is populated with base role data
            // adding roles
            PersonRole[] personRoles = PersonRole.init();
            for (PersonRole role : personRoles) {
                PersonRole existingRole = roleRepo.findByName(role.getName());
                if (existingRole != null) {
                    // role already exists
                    continue;
                } else {
                    // role doesn't exist
                    roleRepo.save(role);
                }
            }

            // Person database is populated with test data
            Person[] personArray = Person.init();
            for (Person person : personArray) {
                //findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase
                List<Person> personFound = personService.list(person.getName(), person.getEmail());  // lookup
                if (personFound.size() == 0) {
                    personService.save(person);  // save

                    personService.addRoleToPerson(person.getEmail(), "ROLE_USER");
                }
            }
            // for testing: giving admin role to Mortensen
            personService.addRoleToPerson(personArray[4].getEmail(), "ROLE_ADMIN");

            // initializing classPeriod objects
            String[] emailsForInit = {"toby@gmail.com", "jm1021@gmail.com"};
            int i = 0;
            ClassPeriod[] classPeriods = ClassPeriod.init();
            for (ClassPeriod classPeriod : classPeriods) {
                ClassPeriod existingClass = classRepo.findByName(classPeriod.getName());
                if (existingClass != null) {
                    // class already exists
                    i++;
                    continue;
                } else {
                    // class doesn't exist
                    classService.save(classPeriod);
                    classService.addLeaderToClass(emailsForInit[i], classPeriod.getName());
                    i++;
                }
            }

            // initializing Assignment objects
            Assignment[] assignments = Assignment.init();
            i = 0;
            for (Assignment ass : assignments) {
                List<Assignment> existingAss = assRepo.findByName(ass.getName());
                if (!(existingAss.isEmpty())) {
                    // ass already exists (it does)
                    i++;
                    continue;
                } else {
                    // ass doesn't exist (it does)
                    assService.save(ass);
                    classService.addAssignmentToClass(ass.getId(), classPeriods[i].getName());
                    i++;
                }
            }
        };
    }
}

