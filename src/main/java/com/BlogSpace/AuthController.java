package com.BlogSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
@Controller
public class AuthController {
	@Autowired private UserRepository userRepo; @Autowired private PasswordEncoder encoder;
	@PostMapping("/signup")
	public String signup(@RequestParam String username, @RequestParam String email, @RequestParam String password, HttpSession session){
		if(userRepo.findByUsername(username)!=null) return "redirect:/?error=user_exists";
		User u = new User(); u.setUsername(username); u.setEmail(email); u.setPassword(encoder.encode(password));
		userRepo.save(u); session.setAttribute("user", u); return "redirect:/dashboard";
	}
	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, HttpSession session){
		User u = userRepo.findByUsername(username);
		if(u!=null && encoder.matches(password, u.getPassword())){ session.setAttribute("user", u); return "redirect:/dashboard"; }
		return "redirect:/?error=invalid";
	}
	@PostMapping("/logout") public String logout(HttpSession s){ s.invalidate(); return "redirect:/"; }
}