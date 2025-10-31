package application.controller;

import application.dto.ColeccionDTO;
import application.dto.HechoDTO;
import application.service.ColeccionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class SolicitudDeEliminacionController {

    private final ColeccionService coleccionService;

    public SolicitudDeEliminacionController(ColeccionService coleccionService) {
            this.coleccionService = coleccionService;
        }

        // 1. Cambia el GetMapping para que acepte un 'hash'
        @GetMapping("/solicitudEliminacion/{hash}")
        public String home(@PathVariable("hash") String hash, Model model) { // 2. Recibe el HASH (String)

            try {
                // 3. Busca la colección global (esto sigue igual)
                List<ColeccionDTO> todasLasColecciones = coleccionService.getAll();

                Optional<Integer> idGlobalOpt = todasLasColecciones.stream()
                        .filter(coleccion -> coleccion.criterioDePertenencia() == null)
                        .map(ColeccionDTO::id)
                        .findFirst();

                if (idGlobalOpt.isEmpty()) {
                    System.err.println("Error: No se encontró una colección global (criterio == null)");
                    return "redirect:/mapa";
                }

                // 4. Traemos TODOS los hechos de esa colección
                List<HechoDTO> hechosGlobales = coleccionService.getHechosDeColeccion(idGlobalOpt.get());

                // 5. ¡LA CLAVE! Filtramos la lista buscando el HASH
                Optional<HechoDTO> hechoBuscado = hechosGlobales.stream()
                        .filter(hecho -> hecho.hash() != null && hecho.hash().equals(hash))
                        .findFirst();

                if (hechoBuscado.isPresent()) {
                    // 6. ¡Éxito! Lo pasamos al modelo
                    model.addAttribute("hecho", hechoBuscado.get());
                } else {
                    System.err.println("Error: El hecho con HASH " + hash + " no se encontró en la colección global.");
                    return "redirect:/mapa";
                }

            } catch (Exception e) {
                System.err.println("Error en SolicitudDeEliminacionController: " + e.getMessage());
                return "redirect:/mapa";
            }

            return "solicitudEliminacion/solicitudEliminacion";
        }
        // (Tu método @PostMapping para recibir el form irá aquí)
        @PostMapping("/solicitudes/eliminacion")
        public String procesarSolicitudDeEliminacion(/* ... */) {
            // ...
            return "redirect:/mapa";
    }
}