package application.controller;

import application.service.FuenteService;
import application.service.SolicitudesEliminacionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import application.service.AdminService;
import org.springframework.security.core.Authentication;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PanelDeControlSolicitudesController {
    private final SolicitudesEliminacionService solicitudesEliminacionService;
    private final AdminService adminService;

    public PanelDeControlSolicitudesController(SolicitudesEliminacionService solicitudesEliminacionService, AdminService adminService) {
        this.solicitudesEliminacionService = solicitudesEliminacionService;
        this.adminService = adminService;
    }

    @GetMapping("/admin/solicitudesEliminacion")
    public String home(Model model, Authentication authentication, HttpServletRequest request, RedirectAttributes ra) {
        if (!adminService.isAdmin(authentication)) {
            ra.addFlashAttribute("popupError", "Esa sección es accesible únicamente para los admins :v");
            String referer = request.getHeader("Referer");
            return "redirect:" + (referer != null ? referer : "/");
        }
        model.addAttribute("listaDeSolicitudes", solicitudesEliminacionService.getAll());
        return "panelDeControl/panelDeControlSolicitudes";
    }


    @PostMapping("/admin/solicitudesEliminacion/{id}/rechazar")
    public String rechazar(@PathVariable("id") Integer id, RedirectAttributes ra, Authentication authentication) {
        if (!adminService.isAdmin(authentication)) {
            ra.addFlashAttribute("toastError", "No tienes permisos para realizar esta acción.");
            return "redirect:/";
        }
        boolean ok = solicitudesEliminacionService.rechazar(id);
        if (ok) {
            ra.addFlashAttribute("toastOk", "Solicitud rechazada.");
        } else {
            ra.addFlashAttribute("toastError", "No se pudo realizar la operación.");
        }
        return "redirect:/admin/solicitudesEliminacion";
    }

    @PostMapping("/admin/solicitudesEliminacion/{id}/aceptar")
    public String aceptar(@PathVariable("id") Integer id, RedirectAttributes ra, Authentication authentication) {
        if (!adminService.isAdmin(authentication)) {
            ra.addFlashAttribute("toastError", "No tienes permisos para realizar esta acción.");
            return "redirect:/";
        }
        boolean ok = solicitudesEliminacionService.aceptar(id);
        if (ok) {
            ra.addFlashAttribute("toastOk", "Solicitud aceptada.");
        } else {
            ra.addFlashAttribute("toastError", "No se pudo realizar la operación.");
        }
        return "redirect:/admin/solicitudesEliminacion";
    }
}
