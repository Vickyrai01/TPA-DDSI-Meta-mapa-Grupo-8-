package application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PanelDeControlController {

    @GetMapping("/admin")
    public String home(Model model) {

        // Si más adelante querés pasar datos a la vista, usá el 'model'
        return "panelDeControl/panelDeControl";
    }

}
