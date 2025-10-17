package servicioEstadisticas.application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import seeders.RepositoryServicioEstadisticasSeeder;
import servicioEstadisticas.model.Hecho;
import servicioEstadisticas.model.ServicioEstadisticas;
import servicioEstadisticas.model.SolicitudSpam;
import servicioEstadisticas.repository.RepositoryServicioEstadisticas;
import utils.DBUtils;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableScheduling
@RestController
@RequestMapping("/servicioEstadisticas")
public class Application {
    private static ServicioEstadisticas servicioEstadisticas = ServicioEstadisticas.getInstance();

    public Application() {
        this.servicioEstadisticas = ServicioEstadisticas.getInstance();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        //RepositoryServicioEstadisticasSeeder repoSeeder = RepositoryServicioEstadisticasSeeder.getInstance();
        //repoSeeder.cargarHechos();
        servicioEstadisticas.actualizarEstadisticas();
    }

    @GetMapping("/health")
    public String health() {
        return "servicio de estadisticas ACTIVA";
    }

    @PostMapping("/hecho")
    public ResponseEntity<String> agregarHecho(@RequestBody Hecho hecho) {

        Hecho hechoABD = new Hecho(
                hecho.getId_hecho(),
                hecho.getCategoria(),
                hecho.getFechaSuceso(),
                hecho.getProvincia()
        );

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
        List<Map<String, Object>> provincias = servicioEstadisticas.getProvinciaConMasHechos();
        return ResponseEntity.ok(provincias);
    }

    @GetMapping("/categoria-mayor-cantidad")
    public ResponseEntity<List<Map<String, Object>>> CategoriaMayorCantidad() {
        List<Map<String, Object>> categorias = servicioEstadisticas.getCategoriaMasReportada();
        return ResponseEntity.ok(categorias);
    }


    @GetMapping("/cantidad-spam")
    public ResponseEntity<Map<String, Object>> cantidadSpam() {
        Map<String, Object> estadisticas = servicioEstadisticas.getCantSolicitudesEliminacion();
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/provincia-con-mas-hechos-por-categoria")
    public ResponseEntity<List<Map<String, Object>>> provinciaConMasHechosPorCategoria(
            @RequestParam(value = "categoria", required = false) String categoria) {
        List<Map<String, Object>> provincias = servicioEstadisticas.provinciaConMasHechosEnCategoria(categoria);
        return ResponseEntity.ok(provincias);
    }

    @GetMapping("/horario-categoria")
    public ResponseEntity<List<Map<String, Object>>> horarioPorCategoria(@RequestParam(value = "categoria", required = false) String categoria) {
        List<Map<String, Object>> horarios = servicioEstadisticas.horarioxCategoria(categoria);
        return ResponseEntity.ok(horarios);
    }

}