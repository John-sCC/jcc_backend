package com.nighthawk.spring_portfolio.mvc.assignment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

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

    // automatic unique identifier for Assignment record
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

    private int points;
    private int allowedSubmissions;

    private List<String> allowedFileTypes = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER)
    private List<AssignmentSubmission> submissions = new ArrayList<>();

    // classes assigned, obsolete bc assignments stored within classes
    /* 
     * @ManyToMany
     * @JoinColumn(name="assignment_to_classes")
     * private Collection<ClassPeriod> classesAssigned = new ArrayList<>()
    */

    // Constructor used when building object from an API
    public Assignment(String name, Date dateCreated, Date dateDue, String content, int points, int allowedSubmissions, String[] allowedFileTypes) {
        this.name = name;
        this.dateCreated = dateCreated;
        this.dateDue = dateDue;
        this.content = content;
        this.points = points;
        this.allowedSubmissions = allowedSubmissions;
        for (String allowedFileType : allowedFileTypes) {
            this.allowedFileTypes.add(allowedFileType);
        }
    }

    // Initialize static test data 
    public static Assignment[] init() {
        // May 30, 2024 at 11:59pm
        Calendar cal1 = Calendar.getInstance();
        cal1.set(2024, Calendar.MAY, 30, 23, 59, 0);
        Date date1 = cal1.getTime();

        // May 28, 2024 at 8:00am
        Calendar cal2 = Calendar.getInstance();
        cal2.set(2024, Calendar.MAY, 28, 8, 0, 0);
        Date date2 = cal2.getTime();

        // May 31, 2024 at 11:59pm
        Calendar cal3 = Calendar.getInstance();
        cal3.set(2024, Calendar.MAY, 31, 23, 59, 0);
        Date date3 = cal3.getTime();

        // Initializing assignments here!!!
        Assignment assignment1 = new Assignment("Teddy's Science Spectacular", new Date(), date1, "Make me feel good about your understanding of science!! Try really hard and I'm certain that you'll succeed. Science science science!", 80, 4, new String[]{"pdf","png","jpg","jpeg"});
        Assignment assignment2 = new Assignment("Big Project Check #3", new Date(), date2, "Expectations: <a href='https://github.com/nighthawkcoders/teacher_portfolio/issues/142'>https://github.com/nighthawkcoders/teacher_portfolio/issues/142</a>", 12, 2, new String[]{"png", "jpg", "jpeg"});
        Assignment assignment3 = new Assignment("Get Out, Seniors", new Date(), date3, "I, Thomas Edison, am so proud of all of the seniors!", 10, 1, new String[]{"pdf"});

        // Array definition and data initialization
        Assignment assignments[] = {assignment1, assignment2, assignment3};
        return(assignments);
    }

    public static void main(String[] args) {
        // obtain Person from initializer
        Assignment assignments[] = init();

        // iterate using "enhanced for loop"
        for (Assignment assignment : assignments) {
            System.out.println(assignment);  // print object
        }
    }

}