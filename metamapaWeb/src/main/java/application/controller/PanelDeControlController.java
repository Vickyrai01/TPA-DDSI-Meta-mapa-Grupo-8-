package application.controller;

import application.service.ColeccionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PanelDeControlController {
    private final ColeccionService coleccionService;
    public PanelDeControlController(ColeccionService coleccionService) {
        this.coleccionService = coleccionService;
    }

    @GetMapping("/admin")
    public String home(Model model) {
        model.addAttribute("listaDeColecciones", coleccionService.getAll());
        return "panelDeControl/panelDeControl";
    }

}
