package application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VerMapaController {
    @GetMapping("/mapa")
    public String verMapa(Model model) {return "verSuceso/verSuceso";
    }
}