package application.controller;

import application.service.ColeccionService;
import application.service.ReportarService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.models.entities.usuario.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ReportarController {
    private final ReportarService reportarService;
    private final ObjectMapper objectMapper;

    public ReportarController(ReportarService reportarService, ObjectMapper objectMapper) {
        this.reportarService = reportarService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/reportar")
    public String reportarSuceso(Model model, @RequestParam(value = "estado", required = false) String estado, Authentication authentication) throws JsonProcessingException {
        // esto hoy te devuelve un String con el JSON
        String categoriasJson = reportarService.getCategorias();

        // lo parseamos a List<String>
        List<String> categorias = objectMapper.readValue(
                categoriasJson,
                new TypeReference<List<String>>() {
                }
        );

        model.addAttribute("categorias", categorias);
        if (estado == null || estado.isBlank()) {
            estado = "form";   // estado por defecto → muestra el formulario
        }
        model.addAttribute("estado", estado);
        // Obtener correo del usuario autenticado
        String email = null;
 if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
            email = oAuth2User.getAttribute("email");
        }

        model.addAttribute("email", email);
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
            @RequestParam(value = "etiquetas", required = false) String etiquetas,
            org.springframework.security.core.Authentication authentication,
            RedirectAttributes ra
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> jsonMap = new HashMap<>();
            String lat = latitud.toString();
            String lon = longitud.toString();
            jsonMap.put("titulo", titulo);
            jsonMap.put("descripcion", descripcion);
            // Obtener el correo del usuario autenticado para el campo contribuyente
            String contribuyente = null;
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User oAuth2User) {
                    contribuyente = oAuth2User.getAttribute("email");
                } else if (principal instanceof java.util.Map<?,?> map) {
                    Object mailObj = map.get("email");
                    if (mailObj instanceof String) {
                        contribuyente = (String) mailObj;
                    }
                }
            }
            jsonMap.put("contribuyente", contribuyente);
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
            List<String> etiquetasList = new ArrayList<>();
            if (etiquetas != null && !etiquetas.isBlank()) {
                etiquetasList = Arrays.stream(etiquetas.trim().split("\\s+"))
                        .filter(s -> !s.isBlank())
                        .toList();
            }
            jsonMap.put("etiquetas", etiquetasList);

            String json = mapper.writeValueAsString(jsonMap);
            ResponseEntity<Void> response = reportarService.postearHecho(json);
            if (response.getStatusCode().is2xxSuccessful()) {
                return "redirect:/reportar?estado=ok";
            } else {
                ra.addFlashAttribute("mensajeError", "El servidor devolvió un estado inesperado.");
                return "redirect:/reportar?estado=error";
            }
        } catch (WebClientResponseException e) {
            // Errores HTTP del servidor remoto (400, 500, etc.)
            e.printStackTrace();
            ra.addFlashAttribute("mensajeError",
                    "Error al conectar con el servidor: " + e.getStatusCode().value());
            return "redirect:/reportar?estado=error";

        } catch (Exception e) {
            // Cualquier otro error (JSON, red, null pointers, etc.)
            e.printStackTrace();
            ra.addFlashAttribute("mensajeError", "Ocurrió un error inesperado.");
            return "redirect:/reportar?estado=error";
        }
    }

    @PostMapping("/ejecutar-agregacion")
    public String ejecutarAgregacion(RedirectAttributes ra) {

        try {
            reportarService.ejecutarAgregacion();
            ra.addFlashAttribute("popupSuccess", "Servicio de agregación ejecutado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("popupError", "Error al ejecutar el servicio de agregación.");
        }

        return "redirect:/reportar"; // O donde quieras volver
    }
}
