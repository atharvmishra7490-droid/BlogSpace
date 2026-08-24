package com.BlogSpace;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String fullName,
            @RequestParam int captchaAnswer,
            HttpSession session) {

        Integer expectedCaptcha = (Integer) session.getAttribute("CAPTCHA_ANSWER");
        if (expectedCaptcha == null || captchaAnswer != expectedCaptcha) {
            return "redirect:/login?error=captcha";
        }

        if (userRepository.findByUsername(username).isPresent()) {
            return "redirect:/login?error=user_exists";
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_USER");
        userRepository.save(user);

        emailService.sendWelcomeEmail(email, username);
        return "redirect:/login?registered=true";
    }
}