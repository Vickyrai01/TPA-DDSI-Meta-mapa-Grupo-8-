package application.controller;

import application.dto.ColeccionDTO;
import application.service.ColeccionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class PanelDeControlController {
    private final ColeccionService coleccionService;

    public PanelDeControlController(ColeccionService coleccionService) {
        this.coleccionService = coleccionService;
    }

    @GetMapping("/admin/colecciones")
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

    //VER
    @PatchMapping("/admin/colecciones/{id}/modificar")
    @ResponseBody
    public ResponseEntity<?> modificarColeccionPatch(
            @PathVariable("id") Integer id,
            @RequestBody Map<String, Object> req
    ) {
        String titulo = req.get("titulo") != null ? req.get("titulo").toString() : null;
        String desc = req.get("descripcionColeccion") != null ? req.get("descripcionColeccion").toString() : null;

        boolean ok = coleccionService.patchColeccion(id, titulo, desc);  // llama al otro backend
        if (ok) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo actualizar");
    }
}

