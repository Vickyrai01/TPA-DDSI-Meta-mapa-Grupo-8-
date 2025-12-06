package application.controller;

import application.dto.HechoDTO;
import application.service.HechoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MisHechosMapaController {
    private final HechoService hechoService;

    public MisHechosMapaController(HechoService hechoService) {
        this.hechoService = hechoService;
    }

    @GetMapping("/mis-hechos/mapa")
    public String verMisHechosEnMapa(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/";
        }
        String nombreCompleto = null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User oAuth2User) {
            String nombre = oAuth2User.getAttribute("name");
            nombreCompleto = nombre;
        } else if (principal instanceof java.util.Map attributes) {
            nombreCompleto = (String) attributes.get("name");
        }
        if (nombreCompleto == null) {
            return "redirect:/";
        }
        List<HechoDTO> hechos = hechoService.getByContribuyente(nombreCompleto);
        model.addAttribute("hechos", hechos);
        return "misHechos/misHechosMapa";
    }
}
