package application.controller;

import application.service.ReportarService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
public class ReportarController {

    private final ReportarService reportarService;
    private final ObjectMapper objectMapper;

    // Almacena la ruta ABSOLUTA y correcta al directorio de subida dentro del módulo
    private final String resolvedUploadPath;

    // Constructor para inyección de dependencias y para resolver la ruta de UPLOADS de forma portable
    public ReportarController(ReportarService reportarService, ObjectMapper objectMapper) {
        this.reportarService = reportarService;
        this.objectMapper = objectMapper;

        // =========================================================
        // LÓGICA DE RESOLUCIÓN DE RUTA PORTABLE (para proyectos multi-módulo)
        // =========================================================
        String pathCalculated = null;
        try {
            // Obtener el directorio de trabajo del proyecto raíz (TPA-DDSI-Meta-mapa-Grupo-8-)
            String rootPath = System.getProperty("user.dir");

            // CONCATENAR la carpeta del MÓDULO y la ruta estática interna
            // **IMPORTANTE: Ajusta "metamapaWeb" si el nombre de tu carpeta de módulo es diferente**
            Path modulePath = Paths.get(rootPath, "metamapaWeb");

            // Construir la ruta final: .../metamapaWeb/src/main/resources/static/uploads/
            pathCalculated = Paths.get(modulePath.toString(), "src/main/resources/static/uploads/").toString();

        } catch (Exception e) {
            // Fallback si no se puede determinar la ruta del módulo
            e.printStackTrace();
            System.err.println("Advertencia: No se pudo resolver la ruta de UPLOADS del módulo. Usando la ruta relativa a la raíz del IDE.");
            pathCalculated = Paths.get(System.getProperty("user.dir"), "src/main/resources/static/uploads/").toString();
        }
        this.resolvedUploadPath = pathCalculated;
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
            // CAMBIO: Recibir el archivo como MultipartFile
            @RequestParam("multimedia") MultipartFile multimediaFile,
            @RequestParam(value = "etiquetas", required = false) String etiquetas,
            @RequestParam(value = "urgente", defaultValue = "false") boolean urgente,
            org.springframework.security.core.Authentication authentication,
            RedirectAttributes ra
    ) {
        String fileNameToSave = null;

        // ===========================================
        // 1. MANEJO Y GUARDADO DEL ARCHIVO MULTIMEDIA
        // ===========================================
        if (multimediaFile != null && !multimediaFile.isEmpty()) {
            try {
                // Usamos la ruta resuelta en el constructor
                Path uploadPath = Paths.get(this.resolvedUploadPath);

                // Crear el directorio si no existe (portable)
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Generar nombre de archivo único para evitar conflictos
                String originalFilename = multimediaFile.getOriginalFilename();
                String fileExtension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                fileNameToSave = UUID.randomUUID().toString() + fileExtension;

                // Guardar el archivo físicamente
                Path filePath = Paths.get(uploadPath.toString(), fileNameToSave);
                Files.copy(multimediaFile.getInputStream(), filePath);

            } catch (IOException e) {
                e.printStackTrace();
                ra.addFlashAttribute("mensajeError", "Error al guardar el archivo multimedia.");
                return "redirect:/reportar?estado=error";
            }
        }

        // ===========================================
        // 2. CONSTRUCCIÓN Y ENVÍO DEL JSON
        // ===========================================
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> jsonMap = new HashMap<>();
            String lat = latitud.toString();
            String lon = longitud.toString();
            jsonMap.put("titulo", titulo);
            jsonMap.put("descripcion", descripcion);

            // Obtener el correo del usuario autenticado
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

            // Mandamos el nombre del archivo guardado
            if (fileNameToSave != null) {
                jsonMap.put("multimedia", List.of(fileNameToSave));
            } else {
                // Si no se subió ningún archivo
                jsonMap.put("multimedia", new ArrayList<>());
            }

            // Procesar etiquetas
            List<String> etiquetasList = new ArrayList<>();
            if (etiquetas != null && !etiquetas.isBlank()) {
                etiquetasList = Arrays.stream(etiquetas.trim().split("\\s+"))
                        .filter(s -> !s.isBlank())
                        .toList();
            }
            jsonMap.put("etiquetas", etiquetasList);

            String json = mapper.writeValueAsString(jsonMap);
            ResponseEntity<Void> response = reportarService.postearHecho(json, urgente);

            if (response.getStatusCode().is2xxSuccessful()) {
                return "redirect:/reportar?estado=ok";
            } else {
                ra.addFlashAttribute("mensajeError", "El servidor devolvió un estado inesperado.");
                return "redirect:/reportar?estado=error";
            }
        } catch (WebClientResponseException e) {
            e.printStackTrace();
            ra.addFlashAttribute("mensajeError",
                    "Error al conectar con el servidor: " + e.getStatusCode().value());
            return "redirect:/reportar?estado=error";

        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("mensajeError", "Ocurrió un error inesperado.");
            return "redirect:/reportar?estado=error";
        }
    }

}
