package com.BlogSpace;
import org.springframework.stereotype.Service;
@Service
public class EmailService{ public void send(String from,String msg){ System.out.println("MAIL to blogspace@gmail.com from "+from+" : "+msg); } }