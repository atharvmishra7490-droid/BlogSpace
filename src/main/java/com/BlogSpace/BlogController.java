package com.BlogSpace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/blogs")
@CrossOrigin
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
}