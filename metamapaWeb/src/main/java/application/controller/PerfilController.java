package application.controller;

import application.service.AdminService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PerfilController {
    private final AdminService adminService;
    public PerfilController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/perfil")
    public String perfil(Model model, Authentication authentication, HttpServletRequest request) {
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
            model.addAttribute("nombre", oAuth2User.getAttribute("name"));
            model.addAttribute("email", oAuth2User.getAttribute("email"));
            model.addAttribute("foto", oAuth2User.getAttribute("picture"));
            model.addAttribute("id", oAuth2User.getName());
            model.addAttribute("proveedor", oAuth2User.getAttribute("iss") != null ? oAuth2User.getAttribute("iss") : "Google");
            model.addAttribute("esAdmin", adminService.isAdmin(authentication));
        }
        String requestedWith = request.getHeader("X-Requested-With");
        if (requestedWith != null && requestedWith.equals("XMLHttpRequest")) {
            return "perfil/perfil :: sidebarPerfil";
        }
        return "redirect:/";
    }
}
