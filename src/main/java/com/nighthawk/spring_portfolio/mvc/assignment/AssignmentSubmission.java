package com.nighthawk.spring_portfolio.mvc.assignment;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Convert;

import com.vladmihalcea.hibernate.type.json.JsonType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.nighthawk.spring_portfolio.mvc.person.Person;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Convert(attributeName ="assignmentSubmission", converter = JsonType.class)
public class AssignmentSubmission {
    // automatic unique identifier for Person record
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Person submitter;

    @NonNull
    private String filePath;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timeSubmitted;

    private int submissionNumber;

    private int score;

    public AssignmentSubmission(Person submitter, String filePath, Date timeSubmitted, int submissionNumber) {
        this.submitter = submitter;
        this.filePath = filePath;
        this.timeSubmitted = timeSubmitted;
        this.submissionNumber = submissionNumber;
        this.score = -1; // score is always initialized to -1 until graded
    }
}
