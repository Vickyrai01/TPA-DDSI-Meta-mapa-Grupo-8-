package application.controller;

import application.dto.ColeccionDTO;
import application.dto.HechoDTO;
import application.service.ColeccionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@Controller
public class VerMapaController {

    private final ColeccionService coleccionService;

    public VerMapaController(ColeccionService coleccionService) {
        this.coleccionService = coleccionService;
    }

    @GetMapping("/mapa")
    public String verMapaGlobal(Model model) {

        Map<String, HechoDTO> hechosUnicos = new HashMap<>();

        try {
            List<ColeccionDTO> todasLasColecciones = coleccionService.getAll();
            for (ColeccionDTO coleccion : todasLasColecciones) {
                List<HechoDTO> hechosDeLaColeccion = coleccionService.getHechosDeColeccion(coleccion.id()); // Asume que ColeccionDTO tiene .id()

                if (hechosDeLaColeccion != null) {
                    for (HechoDTO hecho : hechosDeLaColeccion) {
                        hechosUnicos.put(hecho.hash(), hecho);
                    }
                }
            }

            model.addAttribute("hechos", new ArrayList<>(hechosUnicos.values()));

        } catch (Exception e) {
            model.addAttribute("hechos", List.of());
        }

        return "verSuceso/verSuceso";
    }

    @GetMapping("/colecciones/{id}/mapa")
    public String verMapaPorColeccion(@PathVariable("id") Integer id, Model model) {
        try {
            model.addAttribute("hechos", coleccionService.getHechosDeColeccion(id));
            model.addAttribute("tituloPagina", coleccionService.getById(id).titulo());

        } catch (Exception e) {
            model.addAttribute("hechos", List.of());
        }
        return "verSuceso/verSuceso";
    }
}