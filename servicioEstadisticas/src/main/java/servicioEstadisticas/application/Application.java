package servicioEstadisticas.application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import seeders.RepositoryServicioEstadisticasSeeder;
import servicioEstadisticas.DTO.HechoDTO;
import servicioEstadisticas.GeneradorTodasEstadisticas;
import servicioEstadisticas.model.entities.Hecho;
import servicioEstadisticas.model.entities.SolicitudSpam;
import servicioEstadisticas.model.repository.RepositoryServicioEstadisticas;
import utils.HechoMapperUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableScheduling
@RestController
@RequestMapping("/servicioEstadisticas")
public class Application {
    private static GeneradorTodasEstadisticas generadorTodasEstadisticas = GeneradorTodasEstadisticas.getInstance();

    public Application() {
        this.generadorTodasEstadisticas = GeneradorTodasEstadisticas.getInstance();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        RepositoryServicioEstadisticasSeeder repoSeeder = RepositoryServicioEstadisticasSeeder.getInstance();
        //repoSeeder.cargarHechos();
        generadorTodasEstadisticas.actualizarEstadisticas();
    }

    @GetMapping("/health")
    public String health() {
        return "servicio de estadisticas ACTIVA";
    }

    @PostMapping("/hecho")
    public ResponseEntity<String> agregarHecho(@RequestBody HechoDTO req) {
        System.out.println("Hecho: " + req.toString());
        if (req.getHash() == null || req.getCategoria() == null || req.getProvincia() == null || req.getFecha_suceso() == null) {
            return ResponseEntity.badRequest().body("Faltan campos obligatorios: hash, categoria, provincia o fecha_suceso");
        }
        final Hecho hechoABD;
        try {
            hechoABD = HechoMapperUtils.toEntity(req);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        RepositoryServicioEstadisticas.addHecho(hechoABD);
        return ResponseEntity.status(201).body("Hecho agregado correctamente");
    }

    @PostMapping("/solicitudSpam")
    public ResponseEntity<String> agregarSolicitudSpam(@RequestBody SolicitudSpam solicitudSpam) {

        SolicitudSpam solicitudABD = new SolicitudSpam(
                solicitudSpam.getFueSpam()
        );

        RepositoryServicioEstadisticas.addSolicitud(solicitudABD);
        return ResponseEntity.status(201).body("Solicitud agregado correctamente");
    }

    @GetMapping("/provincia-con-mas-hechos")
    public ResponseEntity<List<Map<String, Object>>> provinciaConMasHechos() {
        List<Map<String, Object>> provincias = generadorTodasEstadisticas.getProvinciaConMasHechos();
        return ResponseEntity.ok(provincias);
    }

    @GetMapping("/categoria-mayor-cantidad")
    public ResponseEntity<List<Map<String, Object>>> CategoriaMayorCantidad() {
        List<Map<String, Object>> categorias = generadorTodasEstadisticas.getCategoriaMasReportada();
        return ResponseEntity.ok(categorias);
    }


    @GetMapping("/cantidad-spam")
    public ResponseEntity<Map<String, Object>> cantidadSpam() {
        Map<String, Object> estadisticas = generadorTodasEstadisticas.getCantSolicitudesEliminacion();
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/provincia-con-mas-hechos-por-categoria")
    public ResponseEntity<List<Map<String, Object>>> provinciaConMasHechosPorCategoria(
            @RequestParam("categoria") String categoria) {

        if (categoria == null || categoria.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        List<Map<String, Object>> provincias =
                generadorTodasEstadisticas.provinciaConMasHechosEnCategoria(categoria);
        return ResponseEntity.ok(provincias);
    }


    @GetMapping("/horario-categoria")
    public ResponseEntity<List<Map<String, Object>>> horarioPorCategoria(@RequestParam("categoria") String categoria) {
        if (categoria == null || categoria.isBlank()) {
            return ResponseEntity.badRequest().build();
        }


        List<Map<String, Object>> horarios = generadorTodasEstadisticas.horarioxCategoria(categoria);
        return ResponseEntity.ok(horarios);
    }

    @GetMapping(value = "/export/csv/provincia-mas-hechos")
    public ResponseEntity<byte[]> exportProvinciaMasHechosCsv() {
        generadorTodasEstadisticas.actualizarEstadisticas();
        List<Map<String, Object>> data = generadorTodasEstadisticas.getProvinciaConMasHechos();

        String csv = toCsv(data, List.of("provincia", "cantidad"));
        return asAttachment(csv, "provincia_mas_hechos.csv");
    }

    @GetMapping(value = "/export/csv/categoria-mas-reportada")
    public ResponseEntity<byte[]> exportCategoriaMasReportadaCsv() {
        generadorTodasEstadisticas.actualizarEstadisticas();
        List<Map<String, Object>> data = generadorTodasEstadisticas.getCategoriaMasReportada();

        String csv = toCsv(data, List.of("categoria", "cantidad"));
        return asAttachment(csv, "categoria_mas_reportada.csv");
    }

    @GetMapping(value = "/export/csv/solicitudes-spam")
    public ResponseEntity<byte[]> exportSolicitudesSpamCsv() {
        generadorTodasEstadisticas.actualizarEstadisticas();
        Map<String, Object> unico = generadorTodasEstadisticas.getCantSolicitudesEliminacion();

        List<Map<String, Object>> data = List.of(unico);
        String csv = toCsv(data, List.of("solicitudes spam", "total de solicitudes"));
        return asAttachment(csv, "solicitudes_spam.csv");
    }

    @GetMapping("/export/csv/horario-por-categoria/{categoria}")
    public ResponseEntity<byte[]> exportHorarioPorCategoriaCsv(@PathVariable String categoria) {
        generadorTodasEstadisticas.actualizarEstadisticas();

        // Normaliza por las dudas
        categoria = java.net.URLDecoder.decode(categoria, StandardCharsets.UTF_8);
        categoria = categoria.replace("+", " ").replace("\"", "");

        List<Map<String, Object>> data = generadorTodasEstadisticas.horarioxCategoria(categoria);
        String csv = toCsv(data, List.of("hora", "cantidad"));

        return asAttachment(csv, "horario_por_categoria_" + categoria + ".csv");
    }

    @GetMapping("/export/csv/provincia-por-categoria/{categoria}")
    public ResponseEntity<byte[]> exportProvinciaPorCategoriaCsv(@PathVariable String categoria) {
        generadorTodasEstadisticas.actualizarEstadisticas();
        List<Map<String, Object>> data = generadorTodasEstadisticas.provinciaConMasHechosEnCategoria(categoria);

        // Normaliza por las dudas
        categoria = java.net.URLDecoder.decode(categoria, StandardCharsets.UTF_8);
        categoria = categoria.replace("+", " ").replace("\"", "");

        String csv = toCsv(data, List.of("provincia", "cantidad"));
        return asAttachment(csv, "provincia_mas_hechos_categoria_" + categoria + ".csv");
    }


    private String toCsv(List<Map<String, Object>> rows, List<String> headers) {
        StringBuilder sb = new StringBuilder();

        sb.append(String.join(",", headers)).append("\n");

        for (Map<String, Object> row : rows) {
            for (int i = 0; i < headers.size(); i++) {
                Object val = row.get(headers.get(i));
                String cell = (val == null) ? "" : String.valueOf(val);
                cell = "\"" + cell.replace("\"", "\"\"") + "\"";
                sb.append(cell);
                if (i < headers.size() - 1) sb.append(",");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private ResponseEntity<byte[]> asAttachment(String csv, String filename) {
        byte[] bytes = csv.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(org.springframework.http.MediaType.valueOf("text/csv"))
                .body(bytes);
    }

}