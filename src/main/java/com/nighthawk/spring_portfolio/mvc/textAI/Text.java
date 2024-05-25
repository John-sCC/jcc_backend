package com.nighthawk.spring_portfolio.mvc.textAI;

import java.time.LocalTime;
import java.time.ZoneId;

public class Text {
    private String name;
    private String text;
    private LocalTime timeUpdated;
    private boolean tested;
    private int score;

    // Constructors (including a no-arg constructor)

    public Text() {
    }

    public Text(String name, String text) {
        this.name = name;
        this.text = text;
        ZoneId temp = ZoneId.of("America/Los_Angeles");
        this.timeUpdated = LocalTime.now(temp);
        this.tested = false;
        this.score = 0;
    }

    // Getters and Setters

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalTime getTimeUpdated() {
        return this.timeUpdated;
    }

    public void setTimeUpdated(LocalTime timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    public boolean getTested() {
        return this.tested;
    }

    public void setTested(boolean tested) {
        this.tested = tested;
    }

    public int getScore() { 
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
