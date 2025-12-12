package cargadorProxy.application;
import cargadorProxy.observabilidad.MetricasCargadorProxy;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
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
import java.time.Duration;
import java.util.List;

@SpringBootApplication(scanBasePackages = "cargadorProxy")
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
    public Mono<ResponseEntity<List<HechoAIntegrarDTO>>> obtenerHechos() {
        return Mono.fromCallable(cargadorProxy::extraerHechosAIntegrar)
                .subscribeOn(Schedulers.boundedElastic())
                .timeout(Duration.ofSeconds(10))
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.ok(List.<HechoAIntegrarDTO>of()));
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
            default -> null;
        };
    }

    @GetMapping("/obtenerFuentes")
    public ResponseEntity<List<Fuente>> obtenerFuentes(){
        List<Fuente> fuentes = repoFuentes.findAll();
        if(fuentes.isEmpty()) return ResponseEntity.status(204).build();
        return ResponseEntity.ok(fuentes);
    }

    @PostMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarFuente(@PathVariable("id") Integer id){
        Fuente fuente = repoFuentes.findById(id);
        if(fuente == null) return ResponseEntity.status(404).build();
        repoFuentes.deleteById(id);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/metricas")
    public ResponseEntity<?> metrics() {
        return ResponseEntity.ok(MetricasCargadorProxy.snapshot());
    }
}