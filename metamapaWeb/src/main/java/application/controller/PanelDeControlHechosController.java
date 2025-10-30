package application.controller;

import application.dto.ColeccionDTO;
import application.dto.HechoDTO;
import application.service.ColeccionService;
import application.service.HechoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class PanelDeControlHechosController {

    private final ColeccionService coleccionService;
    private final HechoService hechoService;

    public PanelDeControlHechosController(ColeccionService coleccionService, HechoService hechoService) {
        this.coleccionService = coleccionService;
        this.hechoService = hechoService;
    }

    // Renderiza Admin Hechos tomando los hechos de la colección global (criterio == null)
    @GetMapping("/admin/hechos")
    public String administrarHechos(Model model) {
        List<ColeccionDTO> colecciones = coleccionService.getAll();

        Optional<Integer> idGlobalOpt = colecciones.stream()
                .filter(c -> c.criterioDePertenencia() == null) // colección sin criterio => global
                .map(ColeccionDTO::id)
                .findFirst();

        List<HechoDTO> hechos = idGlobalOpt
                .map(coleccionService::getHechosDeColeccion) // misma lógica que ver colección
                .orElse(List.of());

        model.addAttribute("listaDeHechos", hechos);
        return "panelDeControl/panelDeControlHECHOS";
    }

    // Eliminar por hash (admin API)
    @PostMapping("/admin/hechos/{hash}/eliminar")
    public String eliminarHecho(@PathVariable("hash") String hash, RedirectAttributes ra) {
        boolean ok = hechoService.deleteByHash(hash);
        ra.addFlashAttribute(ok ? "toastOk" : "toastError",
                ok ? "Hecho eliminado." : "No se pudo eliminar el hecho.");
        return "redirect:/admin/hechos";
    }

    // Editar por hash (admin API)
    @PatchMapping("/admin/hechos/{hash}/modificar")
    @ResponseBody
    public ResponseEntity<?> modificarHecho(@PathVariable("hash") String hash,
                                            @RequestBody Map<String, Object> req) {
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