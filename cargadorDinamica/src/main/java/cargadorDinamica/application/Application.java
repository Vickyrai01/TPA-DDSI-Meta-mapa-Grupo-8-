package cargadorDinamica.application;

import cargadorDinamica.model.CargadorDinamico;
import cargadorDinamica.model.HechoAIntegrarDTO;
import cargadorDinamica.observabilidad.MetricasCargadorDinamico;
import cargadorDinamica.repository.DinamicaRepository;
import cargadorDinamica.repository.RepositoryFuentesSeeder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utils.DBUtils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;


@SpringBootApplication(scanBasePackages = "cargadorDinamica")
@RestController
@RequestMapping("/cargadorDinamico")
public class Application {

    private static RepositoryFuentesSeeder repoFuentesSeeder = RepositoryFuentesSeeder.getInstance();
    private final CargadorDinamico cargadorDinamico;
    private final DinamicaRepository dinamicaRepository;

    // Constructor para inyección de dependencias
    public Application() {
        this.cargadorDinamico = CargadorDinamico.getInstance();
        this.dinamicaRepository = DinamicaRepository.getInstance();
    }

    public static void main(String[] args) {
        repoFuentesSeeder.cargarRepos();
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/health")
    public String health() {
        return "API Cargador Dinámico ACTIVA";
    }

    @GetMapping("/obtenerHechos")
    public ResponseEntity<List<HechoAIntegrarDTO>> obtenerHechos() {
        List<HechoAIntegrarDTO> hechos = cargadorDinamico.extraerHechosAIntegrar();
        return ResponseEntity.ok(hechos);
    }

    @PostMapping("/agregarHecho")
    public ResponseEntity<String> agregarHecho(@RequestBody HechoAIntegrarDTO hecho) {

        HechoAIntegrarDTO hechoDTO = new HechoAIntegrarDTO(
                hecho.getHash(),
                hecho.getTitulo(),
                hecho.getDescripcion(),
                hecho.getCategoria(),
                hecho.getLatitud(),
                hecho.getLongitud(),
                hecho.getFechaSuceso(),
                hecho.getHoraSuceso(),
                hecho.getEtiquetas(),
                hecho.getContribuyente(),
                hecho.getMultimedia()
        );

        hechoDTO.setTipoFuente("DINAMICA");
        hechoDTO.setLinkFuente("Cargado por la web");
        LocalDate fechaHoy = LocalDate.now();
        hechoDTO.setFechaCarga(String.valueOf(fechaHoy));

        dinamicaRepository.save(hechoDTO);
        return ResponseEntity.status(201).body("Hecho agregado correctamente");
    }

    @GetMapping("/metricas")
    public ResponseEntity<?> metrics() {
        return ResponseEntity.ok(MetricasCargadorDinamico.snapshot());
    }

}
