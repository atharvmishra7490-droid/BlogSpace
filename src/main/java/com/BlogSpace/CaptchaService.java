package com.BlogSpace;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import java.util.Random;
@Service
public class CaptchaService {
    public String gen(HttpSession session){
        int a = new Random().nextInt(9)+1;
        int b = new Random().nextInt(9)+1;
        session.setAttribute("captchaAns", a+b);
        return a + " + " + b + " = ?";
    }
    public boolean check(HttpSession session, int ans){
        Object o = session.getAttribute("captchaAns");
        if(o==null) return false;
        return ((Integer)o) == ans;
    }
}