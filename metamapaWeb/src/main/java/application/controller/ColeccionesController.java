package application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import application.service.ColeccionService;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ColeccionesController {
    private final ColeccionService coleccionService;

    public ColeccionesController(ColeccionService coleccionService) {
        this.coleccionService = coleccionService;
    }

    @GetMapping("/colecciones") // Esta es la URL que usará el botón
    public String navegarColecciones(Model model) {

        // (Opcional) Aquí podés buscar datos en tu 'core' y pasarlos a la vista
        model.addAttribute("listaDeColecciones", coleccionService.getAll());

        // Esto le dice a Thymeleaf que busque el archivo:
        // "src/main/resources/templates/navegarColecciones/navegarColecciones.html"
        return "navegarColecciones/navegarColecciones";
    }

    @GetMapping("/colecciones/{id}")
    public String detalle(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("coleccion", coleccionService.getById(id));
        model.addAttribute("hechos", coleccionService.getHechosDeColeccion(id));
        return "verColeccion/verColeccion"; // templates/colecciones/detalle.html
    }

    @GetMapping("/colecciones/{id}/{modoDeNavegacion}/hechos")
    public String verHechosPorConsenso(
            @PathVariable("id") Integer id,
            @PathVariable("tipoConsenso") String tipoConsenso, // <--- CAPTURAMOS EL TIPO
            Model model) {

        // 1. Buscamos la info básica de la colección
        model.addAttribute("coleccion", coleccionService.getById(id));

        // 2. Buscamos los hechos FILTRADOS por el tipo de consenso usando el servicio corregido
        model.addAttribute("hechos", coleccionService.getHechosVisibles(id, tipoConsenso));

        // (Opcional) Es útil pasar el tipo a la vista por si quieres mostrar un título como "Viendo por Votación"
        model.addAttribute("tipoConsensoActual", tipoConsenso);

        return "verColeccion/verColeccion";
    }

}
