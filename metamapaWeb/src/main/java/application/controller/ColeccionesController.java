package application.controller;

import application.dto.ColeccionDTO;
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

        // 1. Buscamos la colección primero para saber su configuración
        ColeccionDTO coleccion = coleccionService.getById(id);
        System.out.println(coleccion);
        System.out.println(coleccion.modoDeNavegacion());

        // 2. Obtenemos el modo de navegación predeterminado de la colección
        // (Asumo que en tu ColeccionDTO el campo se llama algoritmoConsenso, basado en tu método patch)
        String modoPredeterminado = coleccion.modoDeNavegacion();
        if (modoPredeterminado == null || modoPredeterminado.isBlank()) {
            modoPredeterminado = "irrestricto"; // <--- O el valor que use tu Core por defecto
        }

        // 3. Buscamos los hechos usando ESE modo específico
        model.addAttribute("hechos", coleccionService.getHechosVisibles(id, modoPredeterminado));

        // 4. Agregamos la colección y el dato del modo actual al modelo
        model.addAttribute("coleccion", coleccion);

        // Es importante pasar esto por si tu vista usa esta variable para resaltar botones o títulos
        model.addAttribute("tipoConsensoActual", modoPredeterminado);

        return "verColeccion/verColeccion";
    }

}
