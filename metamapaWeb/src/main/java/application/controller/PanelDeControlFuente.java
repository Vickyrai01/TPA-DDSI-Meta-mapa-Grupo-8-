package application.controller;

import application.service.ColeccionService;
import application.service.FuenteService;
import application.service.AdminService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
}
