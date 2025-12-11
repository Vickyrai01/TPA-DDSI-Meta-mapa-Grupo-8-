package application.controller;

import application.dto.HechoDTO;
import application.service.HechoService;
import application.service.ReportarService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MisHechosMapaController {
    private final HechoService hechoService;
    private final ReportarService reportarService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MisHechosMapaController(HechoService hechoService, ReportarService reportarService) {
        this.hechoService = hechoService;
        this.reportarService = reportarService;
    }

    @GetMapping("/mis-hechos/mapa")
    public String verMisHechosEnMapa(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/";
        }

        Object principal = authentication.getPrincipal();

        String email = null;
        String nombreCompleto = null;

        if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User oAuth2User) {
            nombreCompleto = oAuth2User.getAttribute("name");
            email = oAuth2User.getAttribute("email");
        } else if (principal instanceof java.util.Map attributes) {
            nombreCompleto = (String) attributes.get("name");
            email = (String) attributes.get("email");
        }

        // Si no tenemos mail no podemos pedir los hechos filtrados por contribuyente!!!
        if (email == null || email.isBlank()) {
            System.out.println("[MIS-HECHOS] No se encontró email del usuario autenticado");
            model.addAttribute("hechos", java.util.List.of());
        } else {
            List<HechoDTO> hechos = hechoService.getByContribuyente(email);
            model.addAttribute("hechos", hechos);
        }

        model.addAttribute("nombreUsuario", nombreCompleto);

        try {
            String categoriasJson = reportarService.getCategorias();
            java.util.List<String> categorias = objectMapper.readValue(
                    categoriasJson,
                    new TypeReference<java.util.List<String>>() {}
            );
            model.addAttribute("categorias", categorias);
        } catch (Exception e) {
            model.addAttribute("categorias", java.util.List.of());
        }

        return "misHechos/misHechosMapa";
    }
}