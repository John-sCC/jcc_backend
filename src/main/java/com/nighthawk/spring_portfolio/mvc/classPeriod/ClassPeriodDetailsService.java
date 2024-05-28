package com.nighthawk.spring_portfolio.mvc.classPeriod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.nighthawk.spring_portfolio.mvc.person.*;
import com.nighthawk.spring_portfolio.mvc.assignment.*;

/*
This class has an instance of Java Persistence API (JPA)
-- @Autowired annotation. Allows Spring to resolve and inject collaborating beans into our bean.
-- Spring Data JPA will generate a proxy instance
-- Below are some CRUD methods that we can use with our database
*/
@Service
@Transactional
public class ClassPeriodDetailsService implements UserDetailsService {  // "implements" ties ModelRepo to Spring Security
    // Encapsulate many object into a single Bean (Person, Roles, and Scrum)
    @Autowired  // Inject PersonJpaRepository
    private ClassPeriodJpaRepository classPeriodJpaRepository;
    @Autowired  // Inject RoleJpaRepository
    private PersonJpaRepository personJpaRepository;
    @Autowired
    private AssignmentJpaRepository assignmentJpaRepository;
    // @Autowired  // Inject PasswordEncoder
    // PasswordEncoder passwordEncoder(){
    //     return new BCryptPasswordEncoder();
    // }

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

    /* Class fetching Section */

    public  List<ClassPeriod>listAll() {
        return classPeriodJpaRepository.findAllByOrderByNameAsc();
    }

    // custom query to find match to name
    public  List<ClassPeriod>listLike(String name) {
        return classPeriodJpaRepository.findByNameContainingIgnoreCase(name);
    }

    // Method to get class periods by a leader
    public List<ClassPeriod> getClassPeriodsByLeader(Person leader) {
        return classPeriodJpaRepository.findByLeadersContaining(leader);
    }

    // Method to get class periods by a student
    public List<ClassPeriod> getClassPeriodsByStudent(Person student) {
        return classPeriodJpaRepository.findByStudentsContaining(student);
    }

    // Method to get class periods by an assignment
    public List<ClassPeriod> getClassPeriodsByAssignment(Assignment assignment) {
        return classPeriodJpaRepository.findByAssignmentsContaining(assignment);
    }

    // encode password prior to sava
    public void save(ClassPeriod classPeriod) {
        // before the save, a leader MUST be added!
        classPeriodJpaRepository.save(classPeriod);
    }

    public ClassPeriod get(long id) {
        return classPeriodJpaRepository.findById(id);
    }

    public ClassPeriod getByName(String name) {
        return (classPeriodJpaRepository.findByName(name));
    }

    public void delete(long id) {
        classPeriodJpaRepository.deleteById(id);
    }


    /* Leader/Student Section (methods only allowed to leader) */

    public void addLeaderToClass(String personEmail, long classId) { // by passing in the two strings you are giving the class that certain leader
        Person person = personJpaRepository.findByEmail(personEmail);
        if (person != null) {   // verify person
            ClassPeriod classPeriod = classPeriodJpaRepository.findById(classId);
            if (classPeriod != null) { // verify role
                boolean addLeader = true;
                for (Person leader : classPeriod.getLeaders()) {    // only add if class is missing this leader
                    if (leader.getEmail().equals(person.getEmail())) {
                        addLeader = false;
                        break;
                    }
                }
                if (addLeader) classPeriod.getLeaders().add(person);   // everything is valid for adding leader
                // boolean addClass = true;
                // for (ClassPeriod cp : person.getClassPeriods()) {
                //     if (cp.getName().equals(className)) {
                //         addClass = false;
                //         break;
                //     }
                // }
                // if (addClass) person.getClassPeriods().add(classPeriod);
            }
        }
    }

    public void addStudentToClass(String personEmail, long classId) { // by passing in the two strings you are giving the class that certain student
        Person person = personJpaRepository.findByEmail(personEmail);
        if (person != null) {   // verify person
            ClassPeriod classPeriod = classPeriodJpaRepository.findById(classId);
            if (classPeriod != null) { // verify role
                boolean addStudent = true;
                for (Person student : classPeriod.getStudents()) {    // only add if class is missing this student
                    if (student.getEmail().equals(person.getEmail())) {
                        addStudent = false;
                        break;
                    }
                }
                if (addStudent) classPeriod.getStudents().add(person);   // everything is valid for adding student
                // boolean addClass = true;
                // for (ClassPeriod cp : person.getClassPeriods()) {
                //     if (cp.getName().equals(className)) {
                //         addClass = false;
                //         break;
                //     }
                // }
                // if (addClass) person.getClassPeriods().add(classPeriod);
            }
        }
    }
    
    /* Assignment Section (methods only allowed to leader) */
    public void addAssignmentToClass(long assignmentId, long classId) {
        Assignment assignment = assignmentJpaRepository.findById(assignmentId);
        if (assignment != null) {
            ClassPeriod classPeriod = classPeriodJpaRepository.findById(classId);
            if (classPeriod != null) {
                boolean addAssignment = true;
                for (Assignment assLook : classPeriod.getAssignments()) {
                    if (assignment.getId() == assLook.getId()) {
                        addAssignment = false;
                        break;
                    }
                }
                if (addAssignment) classPeriod.getAssignments().add(assignment);
            }
        }
    }
}