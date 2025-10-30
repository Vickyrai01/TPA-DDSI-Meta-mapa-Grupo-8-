package application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class SolicitudDeEliminacionController {

    @GetMapping("/solicitudEliminacion")
    public String home(Model model) {

        return "solicitudEliminacion/solicitudEliminacion";
    }
}
