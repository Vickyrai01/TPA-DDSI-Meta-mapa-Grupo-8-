package application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import application.service.ColeccionService;

@Controller
public class ColeccionesController {
    ColeccionService coleccionService = ColeccionService.getInstance();

    @GetMapping("/colecciones") // Esta es la URL que usará el botón
    public String navegarColecciones(Model model) {

        // (Opcional) Aquí podés buscar datos en tu 'core' y pasarlos a la vista
        model.addAttribute("listaDeColecciones", coleccionService.getAll());

        // Esto le dice a Thymeleaf que busque el archivo:
        // "src/main/resources/templates/navegarColecciones/navegarColecciones.html"
        return "navegarColecciones/navegarColecciones";
    }
}
