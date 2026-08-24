package com.BlogSpace;
import jakarta.persistence.*;
@Entity @Table(name="users")
public class User {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
	@Column(unique=true) private String username;
	@Column(unique=true) private String email;
	private String password; private String phone; private Integer age; private String gender; private String bio;
	public Long getId(){return id;} public void setId(Long id){this.id=id;}
	public String getUsername(){return username;} public void setUsername(String s){this.username=s;}
	public String getEmail(){return email;} public void setEmail(String s){this.email=s;}
	public String getPassword(){return password;} public void setPassword(String s){this.password=s;}
	public String getPhone(){return phone;} public void setPhone(String s){this.phone=s;}
	public Integer getAge(){return age;} public void setAge(Integer s){this.age=s;}
	public String getGender(){return gender;} public void setGender(String s){this.gender=s;}
	public String getBio(){return bio;} public void setBio(String s){this.bio=s;}
}