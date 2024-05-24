package com.nighthawk.spring_portfolio.mvc.person;

public class PersonRequest {
    private String email;
    private String password;
    private String name;
    private String usn;
    private String[] subjectsOfInterest;

    public PersonRequest(String email, String password, String name, String usn, String[] subjectsOfInterest) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.usn = usn;
        this.subjectsOfInterest = subjectsOfInterest;
    }

    // getters and setters
    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getName() {
        return this.name;
    }

    public String getUsn() {
        return this.usn;
    }

    public String[] getSubjectsOfInterest() {
        return this.subjectsOfInterest;
    }
}
