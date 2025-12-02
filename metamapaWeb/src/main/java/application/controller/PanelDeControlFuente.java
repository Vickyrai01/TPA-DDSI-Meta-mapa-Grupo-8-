package application.controller;

import application.service.FuenteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import application.service.AdminService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PanelDeControlFuente {
    private final FuenteService fuenteService;
    private final AdminService adminService;

    @Autowired
    public PanelDeControlFuente(FuenteService fuenteService, AdminService adminService) {
        this.fuenteService = fuenteService;
        this.adminService = adminService;
    }

    @GetMapping("/admin/fuentes")
    public String home(Model model, Authentication authentication, RedirectAttributes ra) {
        if (!adminService.isAdmin(authentication)) {
            ra.addFlashAttribute("toastError", "No podes ingresar porque no sos admin :v");
            return "redirect:/";
        }
        model.addAttribute("listaDeFuentes", fuenteService.getAll());
        return "panelDeControl/panelDeControlFUENTES";
    }

    @PostMapping("/admin/fuentes")
    public String crearFuente(
            @RequestParam("nombre") String nombre,
            @RequestParam("strategyTipoConexion") String formato,          // "API REST" o "CSV"
            @RequestParam(value = "link") String link
          //  @RequestParam(value = "archivoCsv", required = false) MultipartFile archivoCsv
    ) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("nombre", nombre);
        jsonMap.put("link", link);
        jsonMap.put("strategyTipoConexion", formato);

        try {
            String json = mapper.writeValueAsString(jsonMap);
            fuenteService.postearFuente(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "redirect:/admin/fuentes?ok";
    }

    @PostMapping("/admin/fuentes/{id}/eliminar")
    @ResponseBody
    public ResponseEntity<Void> eliminarFuente(@PathVariable("id") Integer id) {

        try {
            fuenteService.eliminarFuente(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).build();
    }
    }
}
