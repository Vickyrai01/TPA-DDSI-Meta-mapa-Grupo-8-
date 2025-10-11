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

@SpringBootApplication
@EnableScheduling
@RestController
@RequestMapping("/servicioEstadisticas")
public class Application {
    private ServicioEstadisticas servicioEstadisticas = ServicioEstadisticas.getInstance();

    public Application() {
        this.servicioEstadisticas = ServicioEstadisticas.getInstance();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        EntityManager em = DBUtils.getEntityManager();
        DBUtils.comenzarTransaccion(em);

        RepositoryServicioEstadisticasSeeder repoSeeder = RepositoryServicioEstadisticasSeeder.getInstance();
        repoSeeder.cargarHechos();
        //Hecho hechoNuevo = new Hecho("ffffjjjjjj","Incendio en lomas de zamora", "2025-10-10", "Buenos Aires");
        //SolicitudSpam soliSpam = new SolicitudSpam(false);

        //em.persist(hechoNuevo);
        //em.persist(soliSpam);

        DBUtils.commit(em);
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
    public ResponseEntity <String> ProvinciaMayorHechos(){
        String provincia = servicioEstadisticas.getProvinciaConMasHechos();
        return ResponseEntity.ok(provincia);
    }

    @GetMapping("/categoria-mayor-cantidad")
    public ResponseEntity <String> CategoriaMayorCantidad(){
        String categoria = servicioEstadisticas.getCategoriaMasReportada();
        return ResponseEntity.ok(categoria);
    }

    @GetMapping("/cantidad-spam")
    public ResponseEntity <Integer> CantidadSpam(){
        Integer cantidad = servicioEstadisticas.getCantSolicitudesEliminacion();
        return ResponseEntity.ok(cantidad);
    }

    @GetMapping("/provincia-con-mas-hechos-por-categoria")
    public ResponseEntity <String> MayorCantHechosCategoria(@RequestParam(value = "categoria", required = false) String categoria){
        String prov = servicioEstadisticas.provicniaConMasHechosEnCategoria(categoria);
        return ResponseEntity.ok(prov);
    }

    @GetMapping("/horario-categoria")
    public ResponseEntity <String> HorarioCategoria(@RequestParam(value = "categoria", required = false) String categoria){
        String horario = servicioEstadisticas.horarioxCategoria(categoria);
        return ResponseEntity.ok(horario);
    }
}