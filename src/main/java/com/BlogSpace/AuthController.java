package com.BlogSpace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired private UserRepository userRepo;
    @Autowired private CaptchaService captchaService;

    @GetMapping("/captcha")
    public Map<String, String> captcha() {
        return captchaService.generate();
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String email = body.get("email");
        String password = body.get("password");
        String captchaId = body.get("captchaId");
        String captchaAnswer = body.get("captchaAnswer");

        Map<String, String> res = new HashMap<>();
        
        if (!captchaService.validate(captchaId, captchaAnswer)) {
            res.put("error", "Invalid captcha");
            return res;
        }
        if (username == null || email == null || password == null || username.isBlank() || email.isBlank() || password.isBlank()) {
            res.put("error", "Fill all fields");
            return res;
        }
        if (userRepo.findByEmail(email).isPresent()) {
            res.put("error", "Email already exists");
            return res;
        }
        if (userRepo.findByUsername(username).isPresent()) {
            res.put("error", "Username already taken");
            return res;
        }

        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(password);
        u.setVerified(true);
        userRepo.save(u);

        res.put("message", "Registered successfully");
        return res;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String captchaId = body.get("captchaId");
        String captchaAnswer = body.get("captchaAnswer");

        Map<String, Object> res = new HashMap<>();

        if (!captchaService.validate(captchaId, captchaAnswer)) {
            res.put("error", "Invalid captcha");
            return res;
        }

        Optional<User> opt = userRepo.findByEmail(email);
        if (opt.isEmpty() || !opt.get().getPassword().equals(password)) {
            res.put("error", "Invalid email or password");
            return res;
        }

        User u = opt.get();
        res.put("id", u.getId());
        res.put("username", u.getUsername());
        res.put("email", u.getEmail());
        res.put("message", "Login success");
        return res;
    }
}