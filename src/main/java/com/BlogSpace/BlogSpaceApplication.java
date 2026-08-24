package com.BlogSpace;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BlogSpaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogSpaceApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedDatabase(UserRepository userRepo, BlogRepository blogRepo, PasswordEncoder encoder) {
        return args -> {
            if (userRepo.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@blogspace.com");
                admin.setPassword(encoder.encode("admin123"));
                admin.setFullName("Platform Admin");
                admin.setBio("BlogSpace Core Super User");
                admin.setRole("ROLE_ADMIN");
                userRepo.save(admin);

                Blog sample = new Blog();
                sample.setTitle("Welcome to BlogSpace 2026");
                sample.setCategory("Technology");
                sample.setContent("BlogSpace is built for engineers and creators. Share your thoughts using modern UI.");
                sample.setAuthor(admin);
                blogRepo.save(sample);
            }
        };
    }
}