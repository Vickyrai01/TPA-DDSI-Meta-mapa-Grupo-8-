package cargadorProxy.application;

import cargadorProxy.model.Fuente;
import cargadorProxy.model.HechoAIntegrarDTO;
import cargadorProxy.model.CargadorProxy;
import cargadorProxy.repository.RepositoryFuentes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/fuentesProxy")
public class Application {

    private final CargadorProxy cargadorProxy;
    private final RepositoryFuentes repoFuentes = RepositoryFuentes.getInstance();

    public Application() {
        this.cargadorProxy = CargadorProxy.getInstance();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/health")
    public String health() {
        return "API Cargador Proxy ACTIVA";
    }

    @GetMapping("/obtenerHechos")
    public ResponseEntity<List<HechoAIntegrarDTO>> obtenerHechos() {
        List<HechoAIntegrarDTO> hechos = cargadorProxy.extraerHechosAIntegrar();
        if(hechos.isEmpty()) return ResponseEntity.status(204).build();
        return ResponseEntity.ok(hechos);
    }

    @PostMapping("/agregarFuente")
    public ResponseEntity<?> agregarFuente(@RequestBody Fuente fuente) {
        repoFuentes.agregarFuente(fuente);
        return ResponseEntity.status(201).body("Fuente guardada correctamente");
    }
}

