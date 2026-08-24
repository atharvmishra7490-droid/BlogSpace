package com.BlogSpace;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    @Autowired UserRepository ur;
    @Autowired CaptchaService cs;
    @Autowired BCryptPasswordEncoder enc;

    @GetMapping("/login")
    public String loginPage(Model m, HttpSession s){
        m.addAttribute("captchaQ", cs.gen(s));
        return "login";
    }
    @PostMapping("/signup")
    public String signup(@RequestParam String username, @RequestParam String email,
                         @RequestParam String password, @RequestParam String gender,
                         @RequestParam Integer age, @RequestParam(required=false) Integer captchaAns,
                         HttpSession s, Model m){
        // Captcha bypass for now
        if(ur.findByUsername(username).isPresent()){
            m.addAttribute("error","Username already taken");
            m.addAttribute("captchaQ",cs.gen(s));
            return "login";
        }
        User u=new User();
        u.setUsername(username); u.setEmail(email);
        u.setPassword(enc.encode(password));
        u.setGender(gender); u.setAge(age);
        ur.save(u);
        s.setAttribute("userId",u.getId());
        s.setAttribute("username",u.getUsername());
        return "redirect:/home";
    }
    @PostMapping("/doLogin")
    public String doLogin(@RequestParam String username, @RequestParam String password,
                          @RequestParam(required=false) Integer captchaAns, HttpSession s, Model m){
        // Captcha bypass for now
        var user=ur.findByUsername(username).orElse(null);
        if(user==null || !enc.matches(password,user.getPassword())){
            m.addAttribute("error","Invalid Username or Password");
            m.addAttribute("captchaQ",cs.gen(s));
            return "login";
        }
        s.setAttribute("userId",user.getId());
        s.setAttribute("username",user.getUsername());
        return "redirect:/home";
    }
    @GetMapping("/logout")
    public String logout(HttpSession s){ s.invalidate(); return "redirect:/"; }
}