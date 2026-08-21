package com.BlogSpace;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 5000)
    private String content;

    private String author; // <-- ADDED FOR NAME

    private LocalDateTime createdAt; // <-- FOR TIMESTAMP

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now(); // auto set time on post
    }

    // Getters and Setters - IMPORTANT, if missing frontend shows blank
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}