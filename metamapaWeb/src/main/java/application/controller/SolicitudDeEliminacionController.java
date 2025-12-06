package application.controller;

import application.dto.ColeccionDTO;
import application.dto.HechoDTO;
import application.service.ColeccionService;
import application.service.HechoService;
import application.service.SolicitudesEliminacionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class SolicitudDeEliminacionController {

    private final ColeccionService coleccionService;
    private final HechoService hechoService;
    private  final SolicitudesEliminacionService solicitudesEliminacionService;

    public SolicitudDeEliminacionController(ColeccionService coleccionService, HechoService hechoService, SolicitudesEliminacionService solicitudesEliminacionService) {
            this.coleccionService = coleccionService;
        this.hechoService = hechoService;
        this.solicitudesEliminacionService = solicitudesEliminacionService;
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
                    // Si no existe la colección "global", hacemos un fallback: pedimos todos los hechos
                    // al admin API y buscamos el hash entre ellos.
                    System.err.println("Advertencia: No se encontró una colección global (criterio == null). Intentando fallback por hechos globales.");
                    List<HechoDTO> todosLosHechos = hechoService.getAll();
                    Optional<HechoDTO> hechoBuscado = todosLosHechos == null ? Optional.empty() : todosLosHechos.stream()
                            .filter(hecho -> hecho.hash() != null && hecho.hash().equals(hash))
                            .findFirst();

                    if (hechoBuscado.isPresent()) {
                        model.addAttribute("hecho", hechoBuscado.get());
                        return "solicitudEliminacion/solicitudEliminacion";
                    } else {
                        System.err.println("Error: El hecho con HASH " + hash + " no se encontró en el fallback de hechos.");
                        return "redirect:/mapa";
                    }
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

        // POST que recibe el form y llama a la API NORMAL (8081)
    @PostMapping("/solicitudes/eliminacion")
    public String procesarSolicitudDeEliminacion(
            @RequestParam("hashHecho") String hashHecho,    // hidden en el form
            @RequestParam("descripcion") String descripcion
    ) {

        boolean ok = solicitudesEliminacionService.crear(hashHecho, descripcion);

        // podés mandar un query param para mostrar mensaje
        if (ok) {
            return "redirect:/mapa?solicitudEliminacion=ok";
        } else {
            return "redirect:/mapa?solicitudEliminacion=error";
        }
    }

}