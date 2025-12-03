package cargadorProxy.application;
import utils.DBUtils;
import javax.persistence.EntityManager;

import cargadorProxy.RepositoryFuentesSeeder;
import cargadorProxy.model.*;
import cargadorProxy.repository.RepositoryFuentes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/cargadorProxy")
public class Application {

    private static RepositoryFuentesSeeder repoFuentesSeeder = RepositoryFuentesSeeder.getInstance();
    private final CargadorProxy cargadorProxy;
    private final RepositoryFuentes repoFuentes = RepositoryFuentes.getInstance();


    public Application() {
        this.cargadorProxy = CargadorProxy.getInstance();
    }

    public static void main(String[] args) {
        repoFuentesSeeder.cargarRepos();
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/health")
    public String health() {
        return "API Cargador Proxy ACTIVA";
    }

    @GetMapping("/obtenerHechos")
    public ResponseEntity<List<HechoAIntegrarDTO>> obtenerHechos() {
        List<HechoAIntegrarDTO> hechos = cargadorProxy.extraerHechosAIntegrar();
        return ResponseEntity.ok(hechos);
    }

    @PostMapping("/agregarFuente")
    public ResponseEntity<?> agregarFuente(@RequestBody FuenteDTO fuenteDTO) {
        StrategyTipoConexion strategyFuente = obtenerStrategyFuente(fuenteDTO.getStrategyTipoConexion());
        if (strategyFuente == null)
            return ResponseEntity.status(400).body("Tipo de fuente no reconocido");
        Fuente fuente = new Fuente(fuenteDTO.getId(), fuenteDTO.getNombre(), fuenteDTO.getLink(), strategyFuente, fuenteDTO.getTipoFuente());
        repoFuentes.save(fuente);
        return ResponseEntity.status(201).build();
    }

    private StrategyTipoConexion obtenerStrategyFuente(String strategyTipoConexion) {
        return switch (strategyTipoConexion) {
            case "BIBLIOTECA" -> new StrategyBibliotecaCliente();
            case "API REST", "APIREST" -> new StrategyAPIREST();
            default -> new StrategyAPIREST();
        };
    }

    @GetMapping("/obtenerFuentes")
    public ResponseEntity<List<Fuente>> obtenerFuentes(){
        List<Fuente> fuentes = repoFuentes.findAll();
        if(fuentes.isEmpty()) return ResponseEntity.status(204).build();
        return ResponseEntity.ok(fuentes);
    }

}