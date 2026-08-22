package com.BlogSpace;
import jakarta.persistence.*;
@Entity @Table(name="users")
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique=true, nullable=false) private String username;
  @Column(unique=true, nullable=false) private String email;
  @Column(nullable=false) private String password;
  private boolean verified=false;
  private String verificationCode;
  public Long getId(){return id;}
  public String getUsername(){return username;} public void setUsername(String u){this.username=u;}
  public String getEmail(){return email;} public void setEmail(String e){this.email=e;}
  public String getPassword(){return password;} public void setPassword(String p){this.password=p;}
  public boolean isVerified(){return verified;} public void setVerified(boolean v){this.verified=v;}
  public String getVerificationCode(){return verificationCode;} public void setVerificationCode(String c){this.verificationCode=c;}
}