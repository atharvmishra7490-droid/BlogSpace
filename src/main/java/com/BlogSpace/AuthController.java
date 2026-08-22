package com.BlogSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/auth") @CrossOrigin
public class AuthController {
  @Autowired private UserRepository userRepo;
  @Autowired private CaptchaService captchaService;
  private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

  @GetMapping("/captcha") 
  public Map<String,String> getCaptcha(){
    return captchaService.generate();
  }

  @PostMapping("/register")
  public Object register(@RequestBody Map<String,String> body){
    // Captcha check
    if(!captchaService.validate(body.get("captchaId"),body.get("captchaAnswer"))) 
      return Map.of("error","Wrong Captcha - answer should be sum, ex 4+5=9");
    
    if(userRepo.findByEmail(body.get("email")).isPresent()) 
      return Map.of("error","Email already used");
    
    User u=new User(); 
    u.setUsername(body.get("username")); 
    u.setEmail(body.get("email"));
    u.setPassword(encoder.encode(body.get("password")));
    u.setVerified(true); // AUTO VERIFIED - no email needed
    u.setVerificationCode(null); 
    userRepo.save(u);
    
    return Map.of("message","Registered Successfully! Please Login","id",u.getId());
  }

  @PostMapping("/login")
  public Object login(@RequestBody Map<String,String> body){
    if(!captchaService.validate(body.get("captchaId"),body.get("captchaAnswer"))) 
      return Map.of("error","Wrong Captcha");
    
    var opt=userRepo.findByEmail(body.get("email"));
    if(opt.isEmpty()||!encoder.matches(body.get("password"),opt.get().getPassword())) 
      return Map.of("error","Invalid email or password");
    
    // REMOVED verification check
    return Map.of("id",opt.get().getId(),"username",opt.get().getUsername(),"email",opt.get().getEmail());
  }

  // You can delete this verify endpoint, but keeping for safety
  @GetMapping("/verify")
  public Object verify(@RequestParam String code){
    return Map.of("message","Verification not required now");
  }
}