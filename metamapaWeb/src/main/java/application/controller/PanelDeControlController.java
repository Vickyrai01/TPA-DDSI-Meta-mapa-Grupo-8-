package application.controller;
import application.service.FuenteService;
import application.dto.ColeccionDTO;
import application.service.ColeccionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import application.service.AdminService;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class PanelDeControlController {
    private final ColeccionService coleccionService;
    private final FuenteService fuenteService;
    private final AdminService adminService;

    public PanelDeControlController(ColeccionService coleccionService, FuenteService fuenteService, AdminService adminService) {
        this.coleccionService = coleccionService;
        this.fuenteService = fuenteService;
        this.adminService = adminService;
    }

    @GetMapping("/admin/colecciones")
    public String home(Model model, Authentication authentication) {
        if (!adminService.isAdmin(authentication)) {
            return "redirect:/";
        }
        model.addAttribute("listaDeColecciones", coleccionService.getAll());
        model.addAttribute("listaDeFuentes", fuenteService.getAll());
        return "panelDeControl/panelDeControl";
    }

    @PostMapping("/admin/colecciones/{id}/eliminar")
    public String eliminar(@PathVariable("id") Integer id, RedirectAttributes ra, Authentication authentication) {
        if (!adminService.isAdmin(authentication)) {
            ra.addFlashAttribute("toastError", "No tienes permisos para realizar esta acción.");
            return "redirect:/";
        }
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
            @RequestBody Map<String, Object> req,
            Authentication authentication
    ) {
        if (!adminService.isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos para realizar esta acción.");
        }
        String titulo = req.get("titulo") != null ? req.get("titulo").toString() : null;
        String desc = req.get("descripcionColeccion") != null ? req.get("descripcionColeccion").toString() : null;
        List<Integer> fuentes = req.get("fuentes") instanceof List<?> list
                ? ((List<?>) list).stream().map(o -> Integer.parseInt(o.toString())).toList()
                : List.of();
        String algoritmoConsenso = req.get("algoritmoConsenso") != null ? req.get("algoritmoConsenso").toString() : null;

        boolean ok = coleccionService.patchColeccion(id, titulo, desc, fuentes, algoritmoConsenso);  // llama al otro backend
        if (ok) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo actualizar");
    }

    @PostMapping("/admin/colecciones/crear")
    @ResponseBody
    public ResponseEntity<?> crearColeccion(@RequestBody Map<String, Object> payload) {

        // Extraemos los datos del JSON que mandó el JavaScript
        String titulo = (String) payload.get("titulo");
        String descripcion = (String) payload.get("descripcionColeccion");

        boolean ok = coleccionService.crearColeccion(payload);

        if (ok) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo crear la colección en el servicio core.");
        }
    }
}

