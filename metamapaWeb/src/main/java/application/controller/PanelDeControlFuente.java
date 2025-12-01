package application.controller;

import application.service.ColeccionService;
import application.service.FuenteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import application.service.AdminService;
import org.springframework.security.core.Authentication;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PanelDeControlFuente {
    private final FuenteService fuenteService;
    private final AdminService adminService;

    public PanelDeControlFuente(FuenteService fuenteService, AdminService adminService) {
        this.fuenteService = fuenteService;
        this.adminService = adminService;
    }

    @GetMapping("/admin/fuentes")
    public String home(Model model, Authentication authentication, HttpServletRequest request, RedirectAttributes ra) {
        if (!adminService.isAdmin(authentication)) {
            ra.addFlashAttribute("popupError", "Esa sección es accesible únicamente para los admins :v");
            String referer = request.getHeader("Referer");
            return "redirect:" + (referer != null ? referer : "/");
        }
        model.addAttribute("listaDeFuentes", fuenteService.getAll());
        return "panelDeControl/panelDeControlFUENTES";
    }
}
