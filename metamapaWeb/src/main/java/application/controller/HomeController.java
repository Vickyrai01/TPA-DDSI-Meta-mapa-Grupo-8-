package application.controller;

import application.service.AdminService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {

    private final AdminService adminService;

    public HomeController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        boolean esAdmin = adminService.isAdmin(authentication);
        model.addAttribute("esAdmin", esAdmin);
        return "home/home";
    }
}