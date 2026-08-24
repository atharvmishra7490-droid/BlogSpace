package com.BlogSpace;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PageController {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CaptchaService captchaService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("blogs", blogRepository.findAll());
        return "home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        return index(model);
    }

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        CaptchaService.CaptchaChallenge challenge = captchaService.generateMathCaptcha();
        session.setAttribute("CAPTCHA_ANSWER", challenge.answer);
        model.addAttribute("captchaQuestion", challenge.question);
        return "login";
    }

    @GetMapping("/explore")
    public String explore(@RequestParam(required = false) String query, Model model) {
        List<Blog> blogs;
        if (query != null && !query.trim().isEmpty()) {
            blogs = blogRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(query, query);
        } else {
            blogs = blogRepository.findAll();
        }
        model.addAttribute("blogs", blogs);
        model.addAttribute("query", query);
        return "explore";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        List<Blog> blogs;
        if (user.getRole().equals("ROLE_ADMIN")) {
            blogs = blogRepository.findAll();
        } else {
            blogs = blogRepository.findByAuthorId(user.getId());
        }
        model.addAttribute("blogs", blogs);
        model.addAttribute("currentUser", user);
        model.addAttribute("totalBlogsCount", blogRepository.count());
        model.addAttribute("userBlogsCount", blogs.size());
        return "dashboard";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("blog", new Blog());
        return "create";
    }

    @GetMapping("/blog/{id}")
    public String blogDetail(@PathVariable Long id, Model model) {
        Blog blog = blogRepository.findById(id).orElseThrow();
        blog.setViews(blog.getViews() + 1);
        blogRepository.save(blog);
        model.addAttribute("blog", blog);
        return "blogDetail";
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute User updatedData, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        user.setFullName(updatedData.getFullName());
        user.setEmail(updatedData.getEmail());
        user.setBio(updatedData.getBio());
        userRepository.save(user);
        return "redirect:/profile?updated=true";
    }
}