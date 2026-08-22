package com.BlogSpace;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {
    private final BlogRepository repo;
    public BlogController(BlogRepository repo){this.repo=repo;}

    @GetMapping 
    public List<Blog> all(){ 
        return repo.findAllByOrderByCreatedAtDesc(); 
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Blog blog){
        if(blog.getTitle() == null || blog.getTitle().isBlank() || blog.getContent() == null || blog.getContent().isBlank()){
            return ResponseEntity.badRequest().body(Map.of("error","Title and Content required"));
        }
        if(blog.getOwnerId() == null){
            return ResponseEntity.status(401).body(Map.of("error","Login required"));
        }
        Blog newBlog = new Blog();
        newBlog.setTitle(blog.getTitle().trim());
        newBlog.setContent(blog.getContent().trim());
        newBlog.setOwnerId(blog.getOwnerId());
        newBlog.setAuthor(blog.getAuthor());
        newBlog.setCreatedAt(LocalDateTime.now());
        return ResponseEntity.ok(repo.save(newBlog));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestParam Long ownerId){
        var opt = repo.findById(id);
        if(opt.isEmpty()) return ResponseEntity.status(404).body(Map.of("error","Not found"));
        // safe null check
        if(opt.get().getOwnerId() == null || !opt.get().getOwnerId().equals(ownerId)){
            return ResponseEntity.status(403).body(Map.of("error","Not your blog"));
        }
        repo.deleteById(id);
        return ResponseEntity.ok(Map.of("message","Deleted"));
    }

    // HIDDEN ADMIN - only you with secret can delete all
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> adminDelete(@PathVariable Long id, @RequestParam String secret){
        if(!"ankur123".equals(secret)) return ResponseEntity.status(403).body(Map.of("error","Wrong secret"));
        if(!repo.existsById(id)) return ResponseEntity.status(404).body(Map.of("error","Not found"));
        repo.deleteById(id);
        return ResponseEntity.ok(Map.of("message","Admin deleted"));
    }
}