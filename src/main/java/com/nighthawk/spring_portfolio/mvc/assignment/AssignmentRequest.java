package com.nighthawk.spring_portfolio.mvc.assignment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

public class AssignmentRequest {
    private String name;
    private Date dateCreated;
    private Date dateDue;
    private int points;
    private String content;
    private String[] classNames;
    private String[] allowedFileTypes;

    public AssignmentRequest(String name, Date dateCreated, Date dateDue, String content, int points, String[] classNames, String[] allowedFileTypes) {
        this.name = name;
        this.dateCreated = dateCreated;
        this.dateDue = dateDue;
        this.content = content;
        this.points = points;
        this.classNames = classNames;
        this.allowedFileTypes = allowedFileTypes;
    }

    public String getName()
    {
        return this.name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public Date getDateCreated()
    {
        return this.dateCreated;
    }

    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String[] getClassNames() {
        return this.classNames;
    }

    public void setClassNames(String[] classNames) {
        this.classNames = classNames;
    }

    public void setDateCreated(String dateCreatedString)
    {
        try {
            this.dateCreated = new SimpleDateFormat("MM-dd-yyyy").parse(dateCreatedString);
        } catch (ParseException e) {
            // Handle the ParseException
            this.dateCreated = getDefaultDate();
            System.out.println("Error parsing date. Using default date.");
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }
    public Date getDateDue()
    {
        return this.dateDue;
    }
    public void setDateDue(String dateDueString)
    {
        try {
            this.dateDue = new SimpleDateFormat("MM-dd-yyyy").parse(dateDueString);
        } catch (ParseException e) {
            // Handle the ParseException
            this.dateDue = getDefaultDate();
            System.out.println("Error parsing date. Using default date.");
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }
    private Date getDefaultDate() 
    {
        try {
            return new SimpleDateFormat("MM-dd-yyyy").parse("12-31-1999");
        } catch (ParseException e) {
            // This should not happen, as the format is known
            e.printStackTrace();
            return null;
        }
    }
    public String getContent()
    {
        return this.content;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String[] getAllowedFileTypes() {
        return this.allowedFileTypes;
    }
    public void setAllowedFileTypes(String[] allowedFileTypes) {
        this.allowedFileTypes = allowedFileTypes;
    }
}
