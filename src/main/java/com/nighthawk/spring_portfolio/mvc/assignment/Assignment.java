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
    @Size(min=0, max=5000, message = "Content (2 to 5000 chars)")
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

        Assignment assignment1 = new Assignment("Teddy's Big Bready", new Date(), new Date(System.currentTimeMillis() + 86400000), "Make me feel good all the time in every way baby wow woohoo!!");
        Assignment assignment2 = new Assignment("Stop YAPPING!!!!", new Date(), new Date(System.currentTimeMillis() + 172800000), "You make me want to... GRRRRR...! MAKE ME CODE NOW!!!!");

        // Array definition and data initialization
        Assignment assignments[] = {assignment1, assignment2};
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