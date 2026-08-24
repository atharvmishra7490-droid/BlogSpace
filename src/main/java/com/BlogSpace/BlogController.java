package com.BlogSpace;
import jakarta.servlet.http.HttpSession; import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; import org.springframework.web.bind.annotation.*;
@Controller
public class BlogController{
    @Autowired BlogRepository br; @Autowired UserRepository ur;
    @PostMapping("/publish") public String publish(@RequestParam String title,@RequestParam String content,HttpSession s){
        Long uid=(Long)s.getAttribute("userId"); if(uid==null) return "redirect:/login";
        User u=ur.findById(uid).get(); Blog b=new Blog(); b.setTitle(title); b.setContent(content); b.setAuthorName(u.getUsername()); b.setUser(u); br.save(b); return "redirect:/dashboard";
    }
    @GetMapping("/delete/{id}") public String del(@PathVariable Long id){ br.deleteById(id); return "redirect:/dashboard"; }
}