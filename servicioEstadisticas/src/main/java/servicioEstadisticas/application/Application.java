package servicioEstadisticas.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import servicioEstadisticas.model.Hecho;
import servicioEstadisticas.model.SolicitudSpam;
import utils.DBUtils;

import javax.persistence.EntityManager;


@SpringBootApplication
@RestController
@RequestMapping("/servicioEstadisticas")
public class Application {
    public Application() {

    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        EntityManager em = DBUtils.getEntityManager();
        DBUtils.comenzarTransaccion(em);

        Hecho hechoNuevo = new Hecho("ffffjjjjjj","Incendio en lomas de zamora", "2025-10-10", "Buenos Aires");
        SolicitudSpam soliSpam = new SolicitudSpam(false);

        em.persist(hechoNuevo);
        em.persist(soliSpam);

        DBUtils.commit(em);
    }

    @GetMapping("/health")
    public String health() {
        return "servicio de estadisticas ACTIVA";
    }

}