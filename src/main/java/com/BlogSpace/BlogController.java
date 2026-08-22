package com.BlogSpace;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blogs")
@CrossOrigin
public class BlogController {
    private final BlogRepository repo;
    public BlogController(BlogRepository repo){this.repo=repo;}

    @GetMapping public List<Blog> all(){ return repo.findAll(); }

    @PostMapping public Blog create(@RequestBody Blog blog){ return repo.save(blog); }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestParam Long ownerId){
        var opt = repo.findById(id);
        if(opt.isEmpty()) return ResponseEntity.status(404).body(Map.of("error","Not found"));
        var b = opt.get();
        if(!b.getOwnerId().equals(ownerId)){
            return ResponseEntity.status(403).body(Map.of("error","You can only delete your own blog"));
        }
        repo.deleteById(id);
        return ResponseEntity.ok(Map.of("message","Deleted"));
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> adminDelete(@PathVariable Long id, @RequestParam String secret){
        if(!"ankur123".equals(secret)) return ResponseEntity.status(403).body("Wrong secret");
        repo.deleteById(id);
        return ResponseEntity.ok("Admin deleted "+id);
    }
}