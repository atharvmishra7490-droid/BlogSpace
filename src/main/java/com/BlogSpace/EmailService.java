package com.BlogSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class EmailService {
  @Autowired(required=false) private JavaMailSender mailSender;
  public void sendVerification(String to,String code){
    if(mailSender==null){System.out.println("MAIL CODE for "+to+" is "+code); return;}
    try{
      SimpleMailMessage m=new SimpleMailMessage();
      m.setTo(to); m.setSubject("BlogSpace - Verify Email");
      m.setText("Your code is: "+code+"\nVerify: /api/auth/verify?code="+code);
      mailSender.send(m);
    }catch(Exception e){System.out.println("Mail failed: "+e.getMessage()+" Code:"+code);}
  }
}