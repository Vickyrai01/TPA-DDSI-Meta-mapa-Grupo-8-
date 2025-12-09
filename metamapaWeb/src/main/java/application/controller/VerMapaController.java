package application.controller;

import application.dto.ColeccionDTO;
import application.dto.HechoDTO;
import application.service.ColeccionService;
import application.service.HechoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class VerMapaController {

    private final ColeccionService coleccionService;
    private final HechoService hechoService;

    public VerMapaController(ColeccionService coleccionService, HechoService hechoService) {
        this.coleccionService = coleccionService;
        this.hechoService = hechoService;
    }

    @GetMapping("/mapa")
    public String verMapaGlobal(Model model) {
        try {
            List<HechoDTO> hechosGlobales = hechoService.getAll();

            // --- AGREGA ESTO PARA PROBAR ---
            System.out.println("--------------------------------------------------");
            System.out.println("DEBUG MAPA: Cantidad de hechos encontrados: " + (hechosGlobales != null ? hechosGlobales.size() : "NULL"));
            if (hechosGlobales != null && !hechosGlobales.isEmpty()) {
                System.out.println("Primer hecho: " + hechosGlobales.get(0).nombre());
                System.out.println("Coordenadas: " + hechosGlobales.get(0).latitud() + ", " + hechosGlobales.get(0).longitud());
            }
            // -------------------------------

            model.addAttribute("hechos", Optional.ofNullable(hechosGlobales).orElse(List.of()));
        } catch (Exception e) {
            System.err.println("Error en verMapaGlobal: " + e.getMessage());
            model.addAttribute("hechos", List.of());
        }
        return "verSuceso/verSuceso";
    }
}