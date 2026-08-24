package com.BlogSpace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/save")
    public String saveBlog(@ModelAttribute Blog blog, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        blog.setAuthor(user);
        blogRepository.save(blog);
        return "redirect:/dashboard";
    }

    @GetMapping("/delete/{id}")
    public String deleteBlog(@PathVariable Long id, Authentication auth) {
        Blog blog = blogRepository.findById(id).orElseThrow();
        User currentUser = userRepository.findByUsername(auth.getName()).orElseThrow();

        // Admin can delete any post. Normal users can only delete their own posts.
        if (currentUser.getRole().equals("ROLE_ADMIN") || blog.getAuthor().getId().equals(currentUser.getId())) {
            blogRepository.delete(blog);
        }
        return "redirect:/dashboard";
    }
}