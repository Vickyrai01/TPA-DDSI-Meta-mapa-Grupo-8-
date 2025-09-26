package cargadorEstatica.application;
import utils.DBUtils;
import javax.persistence.EntityManager;

import cargadorEstatica.model.*;
import cargadorEstatica.repository.RepositoryFuentes;
import cargadorEstatica.repository.RepositoryFuentesSeeder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/cargadorEstatico")
public class Application {

    private static final RepositoryFuentesSeeder repositoryFuentesSeeder = RepositoryFuentesSeeder.getInstance();

    private final CargadorEstatico cargadorEstatico;
    private final RepositoryFuentes repoFuentes = RepositoryFuentes.getInstance();

    public Application() {
        repositoryFuentesSeeder.cargarRepos();
        this.cargadorEstatico = CargadorEstatico.getInstance();
    }

    public static void main(String[] args) {
        repositoryFuentesSeeder.cargarRepos();
        SpringApplication.run(Application.class, args);
        EntityManager em = DBUtils.getEntityManager();
        DBUtils.comenzarTransaccion(em);

        Fuente fuentePrueba = new Fuente("Fuente de prueba 1", "incendios_en_san_luis.csv", new StrategyCSV(), "CSV");
        Fuente fuente2 = new Fuente("Fuente 2", "hechosarevisar.csv", new StrategyCSV(), "CSV");
        em.persist(fuentePrueba);
        em.persist(fuente2);
        DBUtils.commit(em);
    }

    @GetMapping("/health")
    public String health() {
        return "API Cargador Estatico ACTIVA";
    }

    @GetMapping(value = "/obtenerHechos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HechoAIntegrarDTO>> obtenerHechos() {
        List<HechoAIntegrarDTO> hechos = cargadorEstatico.extraerHechosAIntegrar();
        return ResponseEntity.ok(hechos); // 200 con [] si está vacío
    }

    @PostMapping("/agregarFuente")
    public ResponseEntity<?> agregarFuente(@RequestBody FuenteDTO fuenteDTO) {
        StrategyTipoConexion strategyFuente = obtenerStrategyFuente(fuenteDTO.getTipoFuente());
        if (strategyFuente == null) return ResponseEntity.status(400).body("Tipo de fuente no reconocido");
        Fuente fuente = new Fuente(null, fuenteDTO.getNombre(), fuenteDTO.getLink(), strategyFuente, fuenteDTO.getTipoFuente());
        repoFuentes.agregarFuente(fuente);
        return ResponseEntity.status(201).body("Fuente guardada correctamente");
    }

    private StrategyTipoConexion obtenerStrategyFuente(String tipoFuente) {
        if (tipoFuente.equals("CSV")) return new StrategyCSV();
        else return null;
    }

    @GetMapping("/obtenerFuentes")
    public ResponseEntity<List<Fuente>> obtenerFuentes(){
        List<Fuente> fuentes = repoFuentes.getAll();
        if(fuentes.isEmpty()) return ResponseEntity.status(204).build();
        return ResponseEntity.ok(fuentes);
    }

}

