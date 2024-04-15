package com.nighthawk.spring_portfolio.mvc.assignment;

import com.nighthawk.spring_portfolio.mvc.person.Person;

import java.util.Date;

public class AssignmentSubmission {
    private Person submitter;
    private String filePath;
    private Date timeSubmitted;
    private int submissionNumber;

    // basic argument constructor
    public AssignmentSubmission(Person submitter, String filePath, Date timeSubmitted, int submissionNumber) {
        this.submitter = submitter;
        this.filePath = filePath;
        this.timeSubmitted = timeSubmitted;
        this.submissionNumber = submissionNumber;
    }

    // getters
    public Person getSubmitter() {return this.submitter;}
    public String getFilePath() {return this.filePath;}
    public Date getTimeSubmitted() {return this.timeSubmitted;}
    public int getSubmissionNumber() {return this.submissionNumber;}

    // setters
    public void setSubmitter(Person submitter) {this.submitter = submitter;}
    public void setFilePath(String filePath) {this.filePath = filePath;}
    public void setTimeSubmitted(Date timeSubmitted) {this.timeSubmitted = timeSubmitted;}
    public void setSubmissionNumber(int submissionNumber) {this.submissionNumber = submissionNumber;}
}
