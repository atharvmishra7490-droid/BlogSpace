package com.BlogSpace;
import jakarta.servlet.http.HttpSession; import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; import org.springframework.ui.Model; import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
@Controller
public class PageController{
    @Autowired BlogRepository br; @Autowired UserRepository ur; @Autowired EmailService es;
    @GetMapping("/") public String landing(){ return "landing"; }
    @GetMapping("/home") public String home(HttpSession s,Model m){ if(s.getAttribute("userId")==null) return "redirect:/login"; m.addAttribute("username",s.getAttribute("username")); return "home"; }
    @GetMapping("/create") public String create(HttpSession s){ return s.getAttribute("userId")==null?"redirect:/login":"create"; }
    @GetMapping("/explore") public String explore(@RequestParam(required=false) String q,Model m){ m.addAttribute("blogs", q==null||q.isEmpty()?br.findAll():br.findByTitleContainingIgnoreCase(q)); return "explore"; }
    @GetMapping("/dashboard") public String dash(HttpSession s,Model m){
        Long uid=(Long)s.getAttribute("userId"); if(uid==null) return "redirect:/login";
        m.addAttribute("total",br.count()); m.addAttribute("my",br.findByUserId(uid).size());
        m.addAttribute("today",br.findAll().stream().filter(b->b.getCreatedAt().toLocalDate().equals(LocalDate.now())).count());
        m.addAttribute("active",ur.count()); m.addAttribute("myBlogs",br.findByUserId(uid)); return "dashboard";
    }
    @GetMapping("/profile") public String profile(HttpSession s,Model m){ Long uid=(Long)s.getAttribute("userId"); if(uid==null) return "redirect:/login"; m.addAttribute("user",ur.findById(uid).get()); return "profile"; }
    @GetMapping("/settings") public String settings(){ return "settings"; }
    @GetMapping("/blog/{id}") public String detail(@PathVariable Long id,Model m){ m.addAttribute("blog",br.findById(id).orElse(null)); return "blogDetail"; }
    @PostMapping("/help") public String help(@RequestParam String msg,HttpSession s){ es.send((String)s.getAttribute("username"),msg); return "redirect:/profile"; }
    @GetMapping("/deleteAccount") public String delAcc(HttpSession s){ Long uid=(Long)s.getAttribute("userId"); ur.deleteById(uid); s.invalidate(); return "redirect:/"; }
}