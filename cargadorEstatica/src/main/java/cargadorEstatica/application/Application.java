package cargadorEstatica.application;

import cargadorEstatica.model.Fuente;
import cargadorEstatica.model.HechoAIntegrarDTO;
import cargadorEstatica.repository.RepositoryFuentes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cargadorEstatica.model.CargadorEstatico;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/fuentesEstaticas")
public class Application {

    private final CargadorEstatico cargadorEstatico;
    private final RepositoryFuentes repoFuentes = RepositoryFuentes.getInstance();

    public Application() {
        this.cargadorEstatico = CargadorEstatico.getInstance();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/health")
    public String health() {
        return "API Cargador Estatico ACTIVA";
    }

    @GetMapping("/obtenerHechos")
    public ResponseEntity<List<HechoAIntegrarDTO>> obtenerHechos() {
        List<HechoAIntegrarDTO> hechos = cargadorEstatico.extraerHechosAIntegrar();
        if(hechos.isEmpty()) return ResponseEntity.status(204).build();
        return ResponseEntity.ok(hechos);
    }

    @PostMapping("/agregarFuente")
    public ResponseEntity<?> agregarFuente(@RequestBody Fuente fuente) {
        repoFuentes.agregarFuente(fuente);
        return ResponseEntity.status(201).body("Fuente guardada correctamente");
    }
}

