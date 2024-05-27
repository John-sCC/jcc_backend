package com.nighthawk.spring_portfolio.mvc.assignment;

import java.util.Date;

public class SubmissionRequest {
    private long id;
    private Date submissionTime;

    public SubmissionRequest(long id, Date submissionTime) {
        this.id = id;
        this.submissionTime = submissionTime;
    }

    public long getId() {return this.id;}
    public Date getSubmissionTime() {return this.submissionTime;}

    public void setId(long id) {this.id = id;}
    public void setSubmissionTime(Date submissionTime) {this.submissionTime = submissionTime;}
}
