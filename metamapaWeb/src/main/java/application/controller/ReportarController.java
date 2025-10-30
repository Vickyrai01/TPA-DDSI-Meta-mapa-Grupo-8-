package application.controller;

import application.service.ColeccionService;
import application.service.ReportarService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ReportarController {
    private final ReportarService reportarService;
    private final ObjectMapper objectMapper;

    public ReportarController(ReportarService reportarService, ObjectMapper objectMapper) {
        this.reportarService = reportarService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/reportar")
    public String reportarSuceso(Model model, @RequestParam(value= "ok", required = false) String ok) throws JsonProcessingException {
        // esto hoy te devuelve un String con el JSON
        String categoriasJson = reportarService.getCategorias();

        // lo parseamos a List<String>
        List<String> categorias = objectMapper.readValue(
                categoriasJson,
                new TypeReference<List<String>>() {}
        );

        model.addAttribute("categorias", categorias);
        model.addAttribute("ok", ok != null);
        return "reportarSuceso/reportarSuceso";
    }

    @PostMapping("/reportar")
    public String reportarSuceso(
            @RequestParam("titulo") String titulo,
            @RequestParam("categoria") String categoria,
            @RequestParam(value = "categoriaOtra", required = false) String categoriaOtra,
            @RequestParam("fechaSuceso") String fechaSuceso,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("latitud") Double latitud,
            @RequestParam("longitud") Double longitud,
            @RequestParam("multimedia") String multimedia,
            @RequestParam(value = "etiquetas", required = false) List<String> etiquetas
    ) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
        String lat = latitud.toString();
        String lon = longitud.toString();
        jsonMap.put("titulo", titulo);
        jsonMap.put("descripcion", descripcion);
        if (categoria.equals("Otro")) {
            jsonMap.put("categoria", categoriaOtra);
        } else {
            jsonMap.put("categoria", categoria);
        }
        jsonMap.put("latitud", lat);
        jsonMap.put("longitud", lon);
        jsonMap.put("fechaSuceso", fechaSuceso);
        if (multimedia != null && !multimedia.isBlank()) {
            // si tiene contenido, lo mandamos como lista de un solo elemento
            jsonMap.put("multimedia", List.of(multimedia));
        } else {
            // si está vacío, mandamos lista vacía
            jsonMap.put("multimedia", new ArrayList<>());
        }
        //jsonMap.put("etiquetas", etiquetas);
        try {
            String json = mapper.writeValueAsString(jsonMap);
            reportarService.postearHecho(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "redirect:/reportar?ok";
    }
}
