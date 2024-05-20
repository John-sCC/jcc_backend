package com.nighthawk.spring_portfolio.mvc.message;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class MessageApiController {

    // Sample messages
    private List<Message> messages = new ArrayList<>();

    public MessageApiController() {
        // Add some sample messages

        // Sort messages by ID
        Collections.sort(messages, (m1, m2) -> m1.getId().compareTo(m2.getId()));
    }

    @GetMapping("api/messages")
    public List<Message> getAllMessages() {
        return messages;
    }

    @GetMapping("api/messages/{id}")
    public Message getMessageById(@PathVariable Long id) {
        // Find message by ID
        for (Message message : messages) {
            if (message.getId().equals(id)) {
                return message;
            }
        }
        return null; // Message not found
    }

    @PostMapping("/api/messages")
    public void createMessage(@RequestBody Message message) {
        // Logic to save the message (e.g., save it to a database)
        // You can implement this logic based on your requirements
        System.out.println("Received new message: " + message);
        messages.add(new Message(4L, message.getFrom(), message.getTO(), message.getSubject(), message.getContent()));
    }
}
