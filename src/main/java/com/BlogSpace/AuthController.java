package com.BlogSpace;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    
    private final UserRepository userRepo;

    public AuthController(UserRepository r) {
        this.userRepo = r;
    }

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
        String email = body.get("email") == null ? "" : body.get("email").trim();
        String username = body.get("username") == null ? "" : body.get("username").trim();
        String password = body.get("password") == null ? "" : body.get("password").trim();
        String capId = body.get("captchaId");
        String ans = body.get("captchaAnswer");

        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || capId == null || ans == null || ans.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "All fields are required"));
        }
        if (!email.toLowerCase().endsWith(".com")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email must contain .com"));
        }

        if (!captchas.containsKey(capId) || !captchas.get(capId).equals(ans.trim())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Wrong captcha"));
        }
        captchas.remove(capId);

        if (userRepo.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
        }

        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(password);
        userRepo.save(u);
        return ResponseEntity.ok(Map.of("message", "Registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email") == null ? "" : body.get("email").trim();
        String password = body.get("password") == null ? "" : body.get("password").trim();
        String capId = body.get("captchaId");
        String ans = body.get("captchaAnswer") == null ? "" : body.get("captchaAnswer").trim();

        if (email.isEmpty() || password.isEmpty() || capId == null || ans.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "All fields are required"));
        }
        if (!email.toLowerCase().endsWith(".com")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email must contain .com"));
        }
        if (!captchas.containsKey(capId) || !captchas.get(capId).equals(ans)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Wrong captcha"));
        }
        captchas.remove(capId);

        var opt = userRepo.findByEmail(email);
        if (opt.isEmpty() || !opt.get().getPassword().equals(password)) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
        }

        var u = opt.get();
        return ResponseEntity.ok(Map.of("id", u.getId(), "username", u.getUsername(), "email", u.getEmail()));
    }
}