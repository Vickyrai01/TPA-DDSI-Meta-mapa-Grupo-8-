package application.controller;

import application.dto.ColeccionDTO;
import application.dto.HechoDTO;
import application.service.ColeccionService;
import application.service.HechoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import application.service.AdminService;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class PanelDeControlHechosController {

    private final ColeccionService coleccionService;
    private final HechoService hechoService;
    private final AdminService adminService;

    public PanelDeControlHechosController(ColeccionService coleccionService, HechoService hechoService, AdminService adminService) {
        this.coleccionService = coleccionService;
        this.hechoService = hechoService;
        this.adminService = adminService;
    }

    // Renderiza Admin Hechos tomando los hechos de la colección global (criterio == null)
    @GetMapping("/admin/hechos")
    public String administrarHechos(Model model, Authentication authentication, HttpServletRequest request, RedirectAttributes ra) {
        if (!adminService.isAdmin(authentication)) {
            ra.addFlashAttribute("popupError", "Esa sección es accesible únicamente para los admins :v");
            String referer = request.getHeader("Referer");
            return "redirect:" + (referer != null ? referer : "/");
        }
        List<HechoDTO> hechosGlobales = hechoService.getAll();
        model.addAttribute("listaDeHechos", Optional.ofNullable(hechosGlobales).orElse(List.of()));
        return "panelDeControl/panelDeControlHECHOS";
    }

    // Eliminar por hash (admin API)
    @PostMapping("/admin/hechos/{hash}/eliminar")
    public String eliminarHecho(@PathVariable("hash") String hash, RedirectAttributes ra, Authentication authentication) {
        if (!adminService.isAdmin(authentication)) {
            ra.addFlashAttribute("toastError", "No tienes permisos para realizar esta acción.");
            return "redirect:/";
        }
        boolean ok = hechoService.deleteByHash(hash);
        ra.addFlashAttribute(ok ? "toastOk" : "toastError",
                ok ? "Hecho eliminado." : "No se pudo eliminar el hecho.");
        return "redirect:/admin/hechos";
    }

    // Editar por hash (admin API)
    @PatchMapping("/admin/hechos/{hash}/modificar")
    @ResponseBody
    public ResponseEntity<?> modificarHecho(@PathVariable("hash") String hash,
                                            @RequestBody Map<String, Object> req,
                                            Authentication authentication) {
        if (!adminService.isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos para realizar esta acción.");
        }
        String nombre = req.get("nombre") != null ? req.get("nombre").toString() : null;
        String descripcion = req.get("descripcion") != null ? req.get("descripcion").toString() : null;

        List<String> etiquetas = null;
        Object et = req.get("etiquetas");
        if (et instanceof List<?> list) {
            etiquetas = list.stream().map(String::valueOf).toList();
        }

        boolean ok = hechoService.patchByHash(hash, nombre, descripcion, etiquetas);
        return ok ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo actualizar el hecho");
    }
}