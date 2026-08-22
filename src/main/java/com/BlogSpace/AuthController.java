package com.BlogSpace;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    private final UserRepository userRepo;
    public AuthController(UserRepository r){this.userRepo=r;}
    private Map<String, String> captchas = new ConcurrentHashMap<>();

    @GetMapping("/captcha")
    public Map<String,String> captcha(){
        int a = (int)(Math.random()*20+1); int b = (int)(Math.random()*20+1);
        String id = UUID.randomUUID().toString();
        captchas.put(id, String.valueOf(a+b));
        return Map.of("captchaId", id, "question", a+" + "+b+" = ?");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String,String> body){
        String capId = body.get("captchaId"); String ans = body.get("captchaAnswer");
        if(!captchas.containsKey(capId) || !captchas.get(capId).equals(ans))
            return ResponseEntity.badRequest().body(Map.of("error","Wrong captcha"));
        captchas.remove(capId);
        if(userRepo.findByEmail(body.get("email")).isPresent())
            return ResponseEntity.badRequest().body(Map.of("error","Email already exists"));
        User u = new User();
        u.setUsername(body.get("username")); u.setEmail(body.get("email")); u.setPassword(body.get("password"));
        userRepo.save(u);
        return ResponseEntity.ok(Map.of("message","Registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body){
        String capId = body.get("captchaId"); String ans = body.get("captchaAnswer");
        if(!captchas.containsKey(capId) || !captchas.get(capId).equals(ans))
            return ResponseEntity.badRequest().body(Map.of("error","Wrong captcha"));
        captchas.remove(capId);
        var opt = userRepo.findByEmail(body.get("email"));
        if(opt.isEmpty() || !opt.get().getPassword().equals(body.get("password")))
            return ResponseEntity.status(401).body(Map.of("error","Invalid email or password"));
        var u = opt.get();
        return ResponseEntity.ok(Map.of("id", u.getId(),"username", u.getUsername(),"email", u.getEmail()));
    }
}