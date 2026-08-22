package com.BlogSpace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blogs")
@CrossOrigin(origins = "*")
public class BlogController {

    @Autowired
    private BlogRepository repo;

    @GetMapping
    public List<Blog> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public Blog create(@RequestBody Blog blog) {
        return repo.save(blog);
    }

    // Normal users can delete only own, Admin (id=1) can delete any
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlog(@PathVariable Long id, @RequestParam Long ownerId) {
        var blog = repo.findById(id).orElse(null);
        if (blog == null) return ResponseEntity.notFound().build();
        
        boolean isOwner = blog.getOwnerId().equals(ownerId);
        boolean isAdmin = ownerId == 1;
        
        if (!isOwner && !isAdmin) {
            return ResponseEntity.status(403).body(Map.of("error","You can delete only your blog"));
        }
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // Secret admin delete - only you know secret
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> adminDelete(@PathVariable Long id, @RequestParam String secret) {
        if (!"atharv123".equals(secret)) {
            return ResponseEntity.status(403).body("Wrong secret");
        }
        repo.deleteById(id);
        return ResponseEntity.ok().body("Deleted by admin");
    }
}