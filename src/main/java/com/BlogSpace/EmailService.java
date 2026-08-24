package com.BlogSpace;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void sendWelcomeEmail(String to, String username) {
        System.out.println("LOG: Welcome email sent to " + to + " for user: " + username);
    }
}