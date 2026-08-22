package com.BlogSpace;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Blog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 5000)
    private String content;
    private Long ownerId;
    private String author;
    private LocalDateTime createdAt;

    // getters setters
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public String getTitle(){return title;}
    public void setTitle(String t){this.title=t;}
    public String getContent(){return content;}
    public void setContent(String c){this.content=c;}
    public Long getOwnerId(){return ownerId;}
    public void setOwnerId(Long o){this.ownerId=o;}
    public String getAuthor(){return author;}
    public void setAuthor(String a){this.author=a;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public void setCreatedAt(LocalDateTime c){this.createdAt=c;}
}