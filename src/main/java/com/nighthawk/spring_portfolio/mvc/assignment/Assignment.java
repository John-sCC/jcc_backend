package com.nighthawk.spring_portfolio.mvc.assignment;

import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.vladmihalcea.hibernate.type.json.JsonType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/*
Person is a POJO, Plain Old Java Object.
First set of annotations add functionality to POJO
--- @Setter @Getter @ToString @NoArgsConstructor @RequiredArgsConstructor
The last annotation connect to database
--- @Entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Convert(attributeName ="assignment", converter = JsonType.class)
public class Assignment {

    // automatic unique identifier for Person record
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // @NonNull, etc placed in params of constructor: "@NonNull @Size(min = 2, max = 30, message = "Name (2 to 30 chars)") String name"
    @NonNull
    @Size(min = 2, max = 30, message = "Name (2 to 50 chars)")
    private String name;

    //date the assignment is created
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateCreated;

    // the date the assignment is due
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateDue;

    @NonNull
    @Size(min=2, max=5000, message = "Content (2 to 5000 chars)")
    private String content;

    // classes assigned
    /* 
     * @ManyToMany
     * @JoinColumn(name="assignment_to_classes")
     * private Collection<ClassPeriod> classesAssigned = new ArrayList<>()
    */

    // Constructor used when building object from an API
    public Assignment(String name, Date dateCreated, Date dateDue, String content) {
        this.name = name;
        this.dateCreated = dateCreated;
        this.dateDue = dateDue;
        this.content = content;
    }

    // Initialize static test data 
    public static Assignment[] init() {

        // basics of class construction
        Assignment p1 = new Assignment();
        p1.setName("Get a job");
        // p1.setCreatedBy(Person[].class);
        p1.setContent("Get a job you lazy bum");
        // adding Note to notes collection
        try {  // All data that converts formats could fail
            Date d = new SimpleDateFormat("MM-dd-yyyy").parse("00-00-0000");
            p1.setDateCreated(d);
        } catch (Exception e) {
            // no actions as dob default is good enough
        }
        try {
            Date c = new SimpleDateFormat("MM-dd-yyyy").parse("10-10-2025");
            p1.setDateDue(c);
        } catch (Exception e) {}

        Assignment p2 = new Assignment();
        p1.setName("Write a paragraph about Hamlet");
        // p1.setCreatedBy(Person[].class);
        p1.setContent("Tell me about him");
        // adding Note to notes collection
        try {  // All data that converts formats could fail
            Date d = new SimpleDateFormat("MM-dd-yyyy").parse("00-00-0000");
            p1.setDateCreated(d);
        } catch (Exception e) {
            // no actions as dob default is good enough
        }
        try {
            Date c = new SimpleDateFormat("MM-dd-yyyy").parse("10-10-2025");
            p1.setDateDue(c);
        } catch (Exception e) {}

        Assignment p3 = new Assignment();
        p1.setName("Write 5 papers for economics");
        // p1.setCreatedBy(Person[].class);
        p1.setContent("stupid economics");
        // adding Note to notes collection
        try {  // All data that converts formats could fail
            Date d = new SimpleDateFormat("MM-dd-yyyy").parse("00-00-0000");
            p1.setDateCreated(d);
        } catch (Exception e) {
            // no actions as dob default is good enough
        }
        try {
            Date c = new SimpleDateFormat("MM-dd-yyyy").parse("10-10-2025");
            p1.setDateDue(c);
        } catch (Exception e) {}

        // Array definition and data initialization
        Assignment assignments[] = {p1, p2, p3};
        return(assignments);
    }

    public static void main(String[] args) {
        // obtain Person from initializer
        Assignment assignments[] = init();

        // iterate using "enhanced for loop"
        for( Assignment assignment : assignments) {
            System.out.println(assignment);  // print object
        }
    }

}