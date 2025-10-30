package application.controller;

import application.service.FuenteService;
import application.service.SolicitudesEliminacionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PanelDeControlSolicitudesController {
    private final SolicitudesEliminacionService solicitudesEliminacionService;

    public PanelDeControlSolicitudesController(SolicitudesEliminacionService solicitudesEliminacionService) {
        this.solicitudesEliminacionService = solicitudesEliminacionService;
    }

    @GetMapping("/admin/solicitudesEliminacion")
    public String home(Model model) {
        model.addAttribute("listaDeSolicitudes", solicitudesEliminacionService.getAll());
        return "panelDeControl/panelDeControlSolicitudes";
    }


    @PostMapping("/admin/solicitudesEliminacion/{id}/rechazar")
    public String rechazar(@PathVariable("id") Integer id, RedirectAttributes ra) {
        boolean ok = solicitudesEliminacionService.rechazar(id);
        if (ok) {
            ra.addFlashAttribute("toastOk", "Solicitud rechazada.");
        } else {
            ra.addFlashAttribute("toastError", "No se pudo realizar la operación.");
        }
        return "redirect:/admin/solicitudesEliminacion";
    }

    @PostMapping("/admin/solicitudesEliminacion/{id}/aceptar")
    public String aceptar(@PathVariable("id") Integer id, RedirectAttributes ra) {
        boolean ok = solicitudesEliminacionService.aceptar(id);
        if (ok) {
            ra.addFlashAttribute("toastOk", "Solicitud aceptada.");
        } else {
            ra.addFlashAttribute("toastError", "No se pudo realizar la operación.");
        }
        return "redirect:/admin/solicitudesEliminacion";
    }
}
