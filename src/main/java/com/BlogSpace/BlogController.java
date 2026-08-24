package com.BlogSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@CrossOrigin(origins="*")
public class BlogController {
    @Autowired BlogRepository blogRepo;

    @GetMapping
    public List<Blog> getAll(){ return blogRepo.findAllByOrderByCreatedAtDesc(); }

    @PostMapping
    public Blog add(@RequestBody Blog b){ return blogRepo.save(b); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, @RequestParam Long ownerId){
        var b=blogRepo.findById(id).orElse(null);
        if(b!=null && b.getOwnerId().equals(ownerId)) blogRepo.deleteById(id);
    }
    @DeleteMapping("/admin/{id}")
    public void adminDelete(@PathVariable Long id, @RequestParam String secret){
        if("ankur123".equals(secret)) blogRepo.deleteById(id);
    }
}