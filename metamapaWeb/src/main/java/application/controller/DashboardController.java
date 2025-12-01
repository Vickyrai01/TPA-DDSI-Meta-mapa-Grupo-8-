package application.controller;

import application.service.EstadisticasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    EstadisticasService estadisticasService;

    // Vista Dashboard (HTML)
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard/dashboard";
    }

    // Endpoints REST consumidos por JS

    @GetMapping("/dashboard-api/provincia-con-mas-hechos")
    @ResponseBody
    public List<Map> provinciaConMasHechos() {
        return estadisticasService.provinciaConMasHechos();
    }

    @GetMapping("/dashboard-api/categoria-mayor-cantidad")
    @ResponseBody
    public List<Map> categoriaMayorCantidad() {
        return estadisticasService.categoriaMayorCantidad();
    }

    @GetMapping("/dashboard-api/cantidad-spam")
    @ResponseBody
    public Map cantidadSpam() {
        return estadisticasService.cantidadSpam();
    }

    @GetMapping("/dashboard-api/provincia-con-mas-hechos-por-categoria")
    @ResponseBody
    public List<Map> provinciaConMasHechosPorCategoria(@RequestParam String categoria) {
        return estadisticasService.provinciaConMasHechosPorCategoria(categoria);
    }

    @GetMapping("/dashboard-api/horario-por-categoria")
    @ResponseBody
    public List<Map> horarioPorCategoria(@RequestParam String categoria) {
        return estadisticasService.horarioPorCategoria(categoria);
    }
}