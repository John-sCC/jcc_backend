package com.nighthawk.spring_portfolio.mvc.message;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        messages.add(new Message(3L, "sender3@example.com", "Subject 3", "Message 3 content"));
        messages.add(new Message(1L, "sender1@example.com", "Subject 1", "Message 1 content"));
        messages.add(new Message(2L, "sender2@example.com", "Subject 2", "Message 2 content"));

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
}
