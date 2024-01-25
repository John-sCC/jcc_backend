package com.nighthawk.spring_portfolio.mvc.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PersonRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique=true)
    private String name;

    public PersonRole(String name) {
        this.name = name;
    }

    public static PersonRole[] init() { 
        PersonRole student = new PersonRole("ROLE_STUDENT");
        PersonRole admin = new PersonRole("ROLE_ADMIN");
        PersonRole[] initArray = {student, admin};
        return initArray;
    }


}
