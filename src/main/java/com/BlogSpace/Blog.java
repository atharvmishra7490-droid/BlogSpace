package com.BlogSpace;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity
public class Blog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String title; @Column(columnDefinition="TEXT") private String content;
    private String authorName; private LocalDateTime createdAt=LocalDateTime.now();
    @ManyToOne private User user;
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getTitle(){return title;} public void setTitle(String t){this.title=t;}
    public String getContent(){return content;} public void setContent(String c){this.content=c;}
    public String getAuthorName(){return authorName;} public void setAuthorName(String a){this.authorName=a;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime c){this.createdAt=c;}
    public User getUser(){return user;} public void setUser(User u){this.user=u;}
}