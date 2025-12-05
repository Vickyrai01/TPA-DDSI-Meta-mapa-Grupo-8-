package application.controller;

import application.dto.ColeccionDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import application.service.ColeccionService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

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
    public String detalle(
            @PathVariable("id") Integer id,
            @RequestParam(value = "modo", required = false) String modoParam,
            Model model
    ) {

        // 1. Buscamos la colección primero para saber su configuración
        ColeccionDTO coleccion = coleccionService.getById(id);

        // 2. Obtenemos el modo de navegación predeterminado de la colección
        String modoPredeterminado = coleccion.modoDeNavegacion();

        String modoActual = (modoParam != null) ? modoParam : modoPredeterminado;

        if(modoParam != null) {
            if (Objects.equals(modoPredeterminado, "IRRESTRICTA") && Objects.equals(modoParam, "CURADA")) {
                modoActual = "IRRESTRICTA";
            }
            if (!(modoParam.equals("CURADA") || modoParam.equals("IRRESTRICTA"))) {
                modoActual = modoPredeterminado;
            }
        }

        // 3. Buscamos los hechos usando ESE modo específico
        model.addAttribute("hechos", coleccionService.getHechosVisibles(id, modoActual));

        // 4. Agregamos la colección y el dato del modo actual al modelo
        model.addAttribute("coleccion", coleccion);

        // Es importante pasar esto por si tu vista usa esta variable para resaltar botones o títulos
        model.addAttribute("modoActual", modoActual);

        return "verColeccion/verColeccion";
    }

}
