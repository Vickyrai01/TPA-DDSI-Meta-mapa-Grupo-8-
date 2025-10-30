package application.controller;

import application.service.ColeccionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping("/admin/colecciones/{id}/eliminar")
    public String eliminar(@PathVariable("id") Integer id, RedirectAttributes ra) {
        boolean ok = coleccionService.deleteById(id);
        if (ok) {
            ra.addFlashAttribute("toastOk", "Colección eliminada.");
        } else {
            ra.addFlashAttribute("toastError", "No se pudo eliminar la colección.");
        }
        return "redirect:/admin";
    }
}
