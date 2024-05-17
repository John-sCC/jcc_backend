package com.nighthawk.spring_portfolio.mvc.message;


public class Message {
    private Long id;
    private String from;
    private String to;
    private String subject;
    private String content;

    // Constructor with parameters
    public Message(Long id, String from, String to, String subject, String content) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }
    public String getTO() {
        return to;
    }
    public String getFrom() {
        return from;
    }
    public String getSubject() {
        return subject;
    }
    public String getContent() {
        return content;
    }
}
