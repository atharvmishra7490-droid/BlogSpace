package com.BlogSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
@Controller
public class BlogController {
	@Autowired private BlogRepository blogRepo;
	@PostMapping("/addBlog")
	public String addBlog(@RequestParam String title, @RequestParam String content, HttpSession session){
		User u = (User)session.getAttribute("user");
		if(u==null) return "redirect:/";
		Blog b = new Blog(); b.setTitle(title); b.setContent(content); b.setAuthor(u.getUsername()); b.setAuthorUsername(u.getUsername());
		blogRepo.save(b); return "redirect:/explore";
	}
}