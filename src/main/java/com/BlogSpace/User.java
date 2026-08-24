package com.BlogSpace;
import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String gender;
    private Integer age;
    private String photoUrl = "https://i.pravatar.cc/150?u=default";

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getUsername(){return username;} public void setUsername(String s){this.username=s;}
    public String getEmail(){return email;} public void setEmail(String s){this.email=s;}
    public String getPassword(){return password;} public void setPassword(String s){this.password=s;}
    public String getGender(){return gender;} public void setGender(String s){this.gender=s;}
    public Integer getAge(){return age;} public void setAge(Integer a){this.age=a;}
    public String getPhotoUrl(){return photoUrl;} public void setPhotoUrl(String s){this.photoUrl=s;}
}