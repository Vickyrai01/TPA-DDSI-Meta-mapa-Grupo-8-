package servicioEstadisticas.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import servicioEstadisticas.model.Hecho;
import servicioEstadisticas.model.SolicitudSpam;
import servicioEstadisticas.repository.RepositoryServicioEstadisticas;
import utils.DBUtils;

import javax.persistence.EntityManager;


@SpringBootApplication
@EnableScheduling
@RestController
@RequestMapping("/servicioEstadisticas")
public class Application {
    public Application() {

    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        EntityManager em = DBUtils.getEntityManager();
        DBUtils.comenzarTransaccion(em);

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

}