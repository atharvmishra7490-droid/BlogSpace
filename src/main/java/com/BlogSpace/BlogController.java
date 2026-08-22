package com.BlogSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blogs")
@CrossOrigin
public class BlogController {
    @Autowired private BlogRepository repo;

    @GetMapping
    public List<Blog> getAll() { return repo.findAll(); }

    @PostMapping
    public Blog create(@RequestBody Blog blog) { return repo.save(blog); }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlog(@PathVariable Long id, @RequestParam Long ownerId) {
        return repo.findById(id).map(b -> {
            if (!b.getOwnerId().equals(ownerId)) {
                return ResponseEntity.status(403).body(Map.of("error", "Not your blog"));
            }
            repo.deleteById(id);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}