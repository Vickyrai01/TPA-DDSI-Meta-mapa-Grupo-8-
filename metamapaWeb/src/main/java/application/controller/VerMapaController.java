package application.controller;

import application.dto.ColeccionDTO;
import application.dto.HechoDTO;
import application.service.ColeccionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class VerMapaController {

    private final ColeccionService coleccionService;

    public VerMapaController(ColeccionService coleccionService) {
        this.coleccionService = coleccionService;
    }

    @GetMapping("/mapa")
    public String verMapaGlobal(Model model) {
        try {
            List<ColeccionDTO> todasLasColecciones = coleccionService.getAll();

            // Buscamos el ID de la coleccion global (todos los hechos, criterio null)
            Optional<Integer> idGlobalOpt = todasLasColecciones.stream()
                    .filter(coleccion -> coleccion.criterioDePertenencia() == null)
                    .map(ColeccionDTO::id)
                    .findFirst();

            if (idGlobalOpt.isPresent()) {
                Integer idGlobal = idGlobalOpt.get();
                List<HechoDTO> hechosGlobales = coleccionService.getHechosDeColeccion(idGlobal);
                model.addAttribute("hechos", Optional.ofNullable(hechosGlobales).orElse(List.of()));

            } else {
                System.err.println("No se encontró una colección global (criterio == null)");
                model.addAttribute("hechos", List.of());
            }

        } catch (Exception e) {
            System.err.println("Error en verMapaGlobal: " + e.getMessage());
            model.addAttribute("hechos", List.of()); // Lista vacía si falla
        }

        return "verSuceso/verSuceso";
    }
}