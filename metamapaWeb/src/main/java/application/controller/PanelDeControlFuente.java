package application.controller;

import application.service.ColeccionService;
import application.service.FuenteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PanelDeControlFuente {
    private final FuenteService fuenteService;

    public PanelDeControlFuente(FuenteService fuenteService) {
        this.fuenteService = fuenteService;
    }

    @GetMapping("/admin/fuentes")
    public String home(Model model) {
        model.addAttribute("listaDeFuentes", fuenteService.getAll());
        return "panelDeControl/panelDeControlFUENTES";
    }
}
