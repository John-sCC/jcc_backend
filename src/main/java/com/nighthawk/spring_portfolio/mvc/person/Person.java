package com.nighthawk.spring_portfolio.mvc.person;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Convert;
import static jakarta.persistence.FetchType.EAGER;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

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
@Convert(attributeName ="person", converter = JsonType.class)
public class Person {

    // automatic unique identifier for Person record
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // email, password, roles are key attributes to login and authentication
    @NotEmpty
    @Size(min=5)
    @Column(unique=true)
    @Email
    private String email;

    @NotEmpty
    private String password;

    // @NotEmpty
    // private boolean online;


    // @NonNull, etc placed in params of constructor: "@NonNull @Size(min = 2, max = 30, message = "Name (2 to 30 chars)") String name"
    @NonNull
    @Size(min = 2, max = 30, message = "First and Last Name (2 to 30 chars)")
    private String name;

    @NonNull
    @Size(min = 2, max = 20)
    @Column(unique=true)
    private String usn;

    // roles for permissions, different branch
    @ManyToMany(fetch = EAGER)
    private Collection<PersonRole> roles = new ArrayList<>();

    // trying out listing person's classes
    // @ManyToMany(fetch = LAZY)
    // private Collection<ClassPeriod> classPeriods = new ArrayList<>();
    
    // to be implemented later
    /*
     * 
     * @ManyToMany(fetch = LAZY)
     * private Collection<GraphData> statsData = new ArrayList<>();
     * 
     * @ManyToMany(fetch = LAZY)
     * private Collection<QRCode> qrCodes = new ArrayList<>();
     */


    
    // NO NEED FOR ROLES METHODS IN PERSON, all roles add/deletion are handled in other files due to object relationships


    // Constructor used when building object from an API
    public Person(String email, String password, String name, String usn) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.usn = usn;
        // other attributes implemented later
    }

    // Initialize static test data 
    public static Person[] init() {

        // basics of class construction
        Person p1 = new Person();
        p1.setName("Thomas Edison");
        p1.setEmail("toby@gmail.com");
        p1.setPassword("123toby");
        p1.setUsn("bigT");
        // p1.setStatus("online");
        Person p2 = new Person();
        p2.setName("Alexander Graham Bell");
        p2.setEmail("lexb@gmail.com");
        p2.setPassword("123LexB!");
        p2.setUsn("phoneNumber1");
        // p2.setStatus("online");

        Person p3 = new Person();
        p3.setName("Nikola Tesla");
        p3.setEmail("niko@gmail.com");
        p3.setPassword("123Niko!");
        p3.setUsn("iOwnX");
        // p3.setStatus("online");

        Person p4 = new Person();
        p4.setName("Madam Currie");
        p4.setEmail("madam@gmail.com");
        p4.setPassword("123Madam!");
        p4.setUsn("madRadium");
        // p4.setStatus("online");

        Person p5 = new Person();
        p5.setName("John Mortensen");
        p5.setEmail("jm1021@gmail.com");
        p5.setPassword("123Qwerty!");
        p5.setUsn("jMort");
        // p5.setStatus("online");

        Person p6 = new Person();
        p6.setName("Grace Hopper");
        p6.setEmail("hop@gmail.com");
        p6.setPassword("123hop");
        p6.setUsn("mrsComputer");

        // Array definition and data initialization
        Person persons[] = {p1, p2, p3, p4, p5, p6};
        return(persons);
    }

    public static void main(String[] args) {
        // obtain Person from initializer
        Person persons[] = init();

        // iterate using "enhanced for loop"
        for( Person person : persons) {
            System.out.println(person);  // print object
        }
    }

}