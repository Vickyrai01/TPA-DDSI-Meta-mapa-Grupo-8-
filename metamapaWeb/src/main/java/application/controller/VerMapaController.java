package application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VerMapaController {

    @GetMapping("/VerMapa")
    public String home(Model model) {
        // Si más adelante querés pasar datos a la vista, usá el 'model'
        return "home/home";
    }
}