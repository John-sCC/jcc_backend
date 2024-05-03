package com.nighthawk.spring_portfolio.mvc.classPeriod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Convert;
import static jakarta.persistence.FetchType.EAGER;
// import static jakarta.persistence.FetchType.LAZY;
import jakarta.validation.constraints.Size;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.vladmihalcea.hibernate.type.json.JsonType;

import com.nighthawk.spring_portfolio.mvc.person.Person;
import com.nighthawk.spring_portfolio.mvc.assignment.Assignment;

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
@Convert(attributeName ="classPeriod", converter = JsonType.class)
public class ClassPeriod {

    // automatic unique identifier for Person record
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // @NonNull, etc placed in params of constructor: "@NonNull @Size(min = 2, max = 30, message = "Name (2 to 30 chars)") String name"
    @NonNull
    @Column(unique=true)
    @Size(min = 2, max = 50, message = "Name (2 to 50 chars)")
    private String name;

    // leaders in the class can control it; not "teachers" because it could be clubs
    @ManyToMany(fetch = EAGER)
    private Collection<Person> leaders = new ArrayList<>();

    // students in the class have fewer permissions
    @ManyToMany(fetch = EAGER)
    private Collection<Person> students = new ArrayList<>();

    @ManyToMany(fetch = EAGER)
    private Collection<Assignment> assignments = new ArrayList<>();

    /* HashMap is used to store JSON for the classPeriod seating chart
    {
        1: {
            1: "Jeff",
            2: "Paula",
            3: "Ness"
        },
        2: {
            1: "Giygas",
            2: "Sans Undertale"
        }
    }
    */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<Integer,Map<Integer, String>> seatingChart = new HashMap<>(); 
    
    public void setName(String name) {
        this.name = name;
    }

    public void setStudents(ArrayList arr) {
        this.students = arr;
    }

    // Constructor used when building object from an API
    public ClassPeriod(String name) {
        this.name = name;
        // after the new class is created, before save, a leader must be identified by email and added
    }

    // Initialize static test data 
    public static ClassPeriod[] init() {
        ClassPeriod cp1 = new ClassPeriod("T. Edison's Wacky Science Class");
        ClassPeriod cp2 = new ClassPeriod("Mr. Mort's Period 1 CSA");

        // Array definition and data initialization
        ClassPeriod classPeriods[] = {cp1, cp2};
        return(classPeriods);
    }

    public static void main(String[] args) {
        // obtain Person from initializer
        ClassPeriod periods[] = init();

        // iterate using "enhanced for loop"
        for( ClassPeriod period : periods) {
            System.out.println(period);  // print object
        }
    }

}