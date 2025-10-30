package application.controller;

import application.service.FuenteService;
import application.service.SolicitudesEliminacionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PanelDeControlSolicitudesController {
    private final SolicitudesEliminacionService solicitudesEliminacionService;

    public PanelDeControlSolicitudesController(SolicitudesEliminacionService solicitudesEliminacionService) {
        this.solicitudesEliminacionService = solicitudesEliminacionService;
    }

    @GetMapping("/admin/solicitudesEliminacion")
    public String home(Model model) {
        model.addAttribute("listaDeSolicitudes", solicitudesEliminacionService.getAll());
        // Si más adelante querés pasar datos a la vista, usá el 'model'
        return "panelDeControl/panelDeControlSolicitudes";
    }
}
