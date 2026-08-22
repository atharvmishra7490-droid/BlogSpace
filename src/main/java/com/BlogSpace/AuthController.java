package com.BlogSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/auth") @CrossOrigin
public class AuthController {
  @Autowired private UserRepository userRepo;
  @Autowired private CaptchaService captchaService;
  @Autowired private EmailService emailService;
  private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
  @GetMapping("/captcha") public Map<String,String> getCaptcha(){return captchaService.generate();}
  @PostMapping("/register")
  public Object register(@RequestBody Map<String,String> body){
    if(!captchaService.validate(body.get("captchaId"),body.get("captchaAnswer"))) return Map.of("error","Wrong Captcha");
    if(userRepo.findByEmail(body.get("email")).isPresent()) return Map.of("error","Email already used");
    User u=new User(); u.setUsername(body.get("username")); u.setEmail(body.get("email"));
    u.setPassword(encoder.encode(body.get("password")));
    String code=UUID.randomUUID().toString(); u.setVerificationCode(code); userRepo.save(u);
    emailService.sendVerification(u.getEmail(),code);
    return Map.of("message","Registered. Verification code: "+code,"id",u.getId());
  }
  @GetMapping("/verify")
  public Object verify(@RequestParam String code){
    var opt=userRepo.findByVerificationCode(code);
    if(opt.isEmpty()) return Map.of("error","Invalid code");
    User u=opt.get(); u.setVerified(true); u.setVerificationCode(null); userRepo.save(u);
    return Map.of("message","Email verified! Login now");
  }
  @PostMapping("/login")
  public Object login(@RequestBody Map<String,String> body){
    if(!captchaService.validate(body.get("captchaId"),body.get("captchaAnswer"))) return Map.of("error","Wrong Captcha");
    var opt=userRepo.findByEmail(body.get("email"));
    if(opt.isEmpty()||!encoder.matches(body.get("password"),opt.get().getPassword())) return Map.of("error","Invalid email or password");
    if(!opt.get().isVerified()) return Map.of("error","Verify email first");
    return Map.of("id",opt.get().getId(),"username",opt.get().getUsername(),"email",opt.get().getEmail());
  }
}