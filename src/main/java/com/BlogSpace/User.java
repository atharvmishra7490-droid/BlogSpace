package com.BlogSpace;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name="users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,20}$", 
             message = "Username must have both letters & numbers (4-20 chars)")
    @Column(unique = true, nullable = false)
    private String username;
    
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)\\.com$", 
             message = "Email must be valid and end with .com")
    @Column(unique=true, nullable=false)
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min=6, message="Password must be at least 6 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{6,}$",
             message="Password must have letters and numbers")
    private String password;

    private String phone;
    @Min(value=13, message="Age must be 13+") @Max(value=100)
    private Integer age;
    private String gender;
    private String bio;
    private String location;
    @Column(length=1000)
    private String photoUrl = "https://i.pravatar.cc/300";
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters Setters
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getUsername(){return username;} public void setUsername(String u){this.username=u;}
    public String getEmail(){return email;} public void setEmail(String e){this.email=e;}
    public String getPassword(){return password;} public void setPassword(String p){this.password=p;}
    public String getPhone(){return phone;} public void setPhone(String p){this.phone=p;}
    public Integer getAge(){return age;} public void setAge(Integer a){this.age=a;}
    public String getGender(){return gender;} public void setGender(String g){this.gender=g;}
    public String getBio(){return bio;} public void setBio(String b){this.bio=b;}
    public String getLocation(){return location;} public void setLocation(String l){this.location=l;}
    public String getPhotoUrl(){return photoUrl;} public void setPhotoUrl(String s){this.photoUrl=s;}
    public LocalDateTime getCreatedAt(){return createdAt;}
}