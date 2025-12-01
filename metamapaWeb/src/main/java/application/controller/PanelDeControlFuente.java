package application.controller;

import application.service.ColeccionService;
import application.service.FuenteService;
import core.api.DTO.FuenteDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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
