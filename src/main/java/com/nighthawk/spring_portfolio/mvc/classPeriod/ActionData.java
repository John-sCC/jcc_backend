package com.nighthawk.spring_portfolio.mvc.classPeriod;

public class ActionData {
    private String type; // Type of action (e.g., ADD_STUDENT, ADD_INSTRUCTOR)
    private String id; // ID of the action (e.g., student ID, instructor ID)
    private String name; // Name associated with the action (e.g., student name, instructor name)
    private String email; // Email associated with the action (e.g., student email, instructor email)

    // Constructors, getters, and setters
    public ActionData() {
        // Default constructor
    }

    public ActionData(String type, String id, String name, String email) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getters and setters for each field
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // toString method for debugging or logging
    @Override
    public String toString() {
        return "ActionData{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
