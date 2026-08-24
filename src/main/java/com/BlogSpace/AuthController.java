package com.BlogSpace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins="*")
public class AuthController {
    
    @Autowired UserRepository userRepo;
    @Autowired PasswordEncoder encoder;

    private Map<String, String> captchas = new ConcurrentHashMap<>();

    @GetMapping("/captcha")
    public Map<String, String> captcha() {
        int a = (int) (Math.random() * 20 + 1);
        int b = (int) (Math.random() * 20 + 1);
        String id = UUID.randomUUID().toString();
        captchas.put(id, String.valueOf(a + b));
        return Map.of("captchaId", id, "question", a + " + " + b + " = ?");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String email = body.get("email")==null?"":body.get("email").trim();
        String username = body.get("username")==null?"":body.get("username").trim();
        String password = body.get("password")==null?"":body.get("password").trim();
        String capId = body.get("captchaId");
        String ans = body.get("captchaAnswer");

        // 1. Blank check
        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || capId==null || ans==null || ans.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "All fields are required"));
        }
        // 2. Email must be like ...@...com
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)\\.com$")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email must be valid and end with .com"));
        }
        // 3. Username must have letters + numbers both
        if (!username.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,20}$")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username must have both letters and numbers (4-20 chars) e.g. john123"));
        }
        // 4. Password 6 chars alphanumeric with letters+numbers
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{6,}$")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Password must be 6+ chars with both letters and numbers"));
        }
        // 5. Captcha
        if (!captchas.containsKey(capId) || !captchas.get(capId).equals(ans.trim())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Wrong captcha"));
        }
        captchas.remove(capId);

        if (userRepo.findByEmail(email).isPresent()) return ResponseEntity.badRequest().body(Map.of("error","Email already exists"));
        if (userRepo.findByUsername(username).isPresent()) return ResponseEntity.badRequest().body(Map.of("error","Username already taken"));

        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(encoder.encode(password)); // Encrypted
        userRepo.save(u);
        return ResponseEntity.ok(Map.of("message", "Registered Successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email")==null?"":body.get("email").trim();
        String password = body.get("password")==null?"":body.get("password").trim();
        String capId = body.get("captchaId");
        String ans = body.get("captchaAnswer")==null?"":body.get("captchaAnswer").trim();

        if (email.isEmpty() || password.isEmpty() || capId==null || ans.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "All fields are required"));
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)\\.com$")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email must be valid .com"));
        }
        if (!captchas.containsKey(capId) || !captchas.get(capId).equals(ans)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Wrong captcha"));
        }
        captchas.remove(capId);

        var opt = userRepo.findByEmail(email);
        if (opt.isEmpty() || !encoder.matches(password, opt.get().getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
        }

        var u = opt.get();
        return ResponseEntity.ok(Map.of(
            "id", u.getId(), 
            "username", u.getUsername(), 
            "email", u.getEmail(),
            "photoUrl", u.getPhotoUrl()!=null?u.getPhotoUrl():""
        ));
    }
}