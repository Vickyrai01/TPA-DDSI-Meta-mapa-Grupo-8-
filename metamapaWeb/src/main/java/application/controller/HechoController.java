package application.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HechoController {
    @GetMapping("/reportar")
    public String reportarSuceso(Model model) {

        return "reportarSuceso/reportarSuceso";
    }
}
