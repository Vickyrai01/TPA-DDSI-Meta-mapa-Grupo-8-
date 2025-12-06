package application.controller;

import application.dto.HechoDTO;
import application.service.HechoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MisHechosController {
    private final HechoService hechoService;

    public MisHechosController(HechoService hechoService) {
        this.hechoService = hechoService;
    }

    @GetMapping("/mis-hechos")
    public String verMisHechos(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/";
        }
        String email = null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User oAuth2User) {
            email = oAuth2User.getAttribute("email");
        } else if (principal instanceof java.util.Map attributes) {
            email = (String) attributes.get("email");
        }
        if (email == null) {
            return "redirect:/";
        }
        List<HechoDTO> hechos = hechoService.getByContribuyente(email);
        model.addAttribute("misHechos", hechos);
        return "perfil/misHechos";
    }
}
