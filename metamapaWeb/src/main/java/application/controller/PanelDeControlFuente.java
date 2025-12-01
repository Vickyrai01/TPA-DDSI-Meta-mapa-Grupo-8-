package application.controller;

import application.service.ColeccionService;
import application.service.FuenteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.api.DTO.FuenteDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

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

}
