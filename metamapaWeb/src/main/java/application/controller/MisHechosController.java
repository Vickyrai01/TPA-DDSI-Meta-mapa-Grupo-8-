package application.controller;

import application.dto.HechoDTO;
import application.service.HechoService;
import application.service.ReportarService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class MisHechosController {
    private final HechoService hechoService;
    private final ReportarService reportarService;

    public MisHechosController(HechoService hechoService, ReportarService reportarService) {
        this.hechoService = hechoService;
        this.reportarService = reportarService;
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

    // Mostrar formulario de edición
    @GetMapping("/misHechos/editar/{hash}")
    public String mostrarFormularioEdicion(@PathVariable("hash") String hash, Model model, Authentication authentication) throws com.fasterxml.jackson.core.JsonProcessingException {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/";
        }
        HechoDTO hecho = hechoService.getAll().stream()
                .filter(h -> h.hash().equals(hash))
                .findFirst()
                .orElse(null);
        if (hecho == null) {
            return "redirect:/perfil/misHechos";
        }
        // Obtener categorías disponibles
        String categoriasJson = reportarService.getCategorias();
        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        java.util.List<String> categorias = objectMapper.readValue(categoriasJson, new com.fasterxml.jackson.core.type.TypeReference<java.util.List<String>>() {});
        model.addAttribute("hecho", hecho);
        model.addAttribute("categorias", categorias);
        return "perfil/editarHecho";
    }

    // Procesar edición
    @PostMapping("/misHechos/editar/{hash}")
    public String editarHecho(@PathVariable("hash") String hash,
                              @ModelAttribute("hecho") HechoDTO hechoEditado,
                              Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/";
        }
        boolean ok = hechoService.patchByHash(hash, hechoEditado.nombre(), hechoEditado.descripcion(), hechoEditado.etiquetas());
        // Si quieres actualizar fechaSuceso, deberás modificar el servicio y el backend
        return "redirect:/perfil/misHechos";
    }
}
