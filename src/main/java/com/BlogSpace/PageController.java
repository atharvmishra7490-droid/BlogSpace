package com.BlogSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class PageController {
	@Autowired private BlogRepository blogRepo;
	@GetMapping("/") public String home(Model m){ m.addAttribute("blogs", blogRepo.findAll()); return "home"; }
	@GetMapping("/explore") public String explore(Model m){ m.addAttribute("blogs", blogRepo.findAll()); return "explore"; }
	@GetMapping("/dashboard") public String dashboard(){ return "dashboard"; }
	@GetMapping("/create") public String create(){ return "create"; }
	@GetMapping("/profile") public String profile(){ return "profile"; }
}