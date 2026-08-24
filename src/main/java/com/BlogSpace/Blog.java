package com.BlogSpace;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
public class Blog {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
	private String title; @Column(length=5000) private String content; private String author; private String authorUsername;
	private LocalDateTime createdAt = LocalDateTime.now();
	public Long getId(){return id;} public void setId(Long id){this.id=id;}
	public String getTitle(){return title;} public void setTitle(String t){this.title=t;}
	public String getContent(){return content;} public void setContent(String c){this.content=c;}
	public String getAuthor(){return author;} public void setAuthor(String a){this.author=a;}
	public String getAuthorUsername(){return authorUsername;} public void setAuthorUsername(String a){this.authorUsername=a;}
	public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime c){this.createdAt=c;}
}