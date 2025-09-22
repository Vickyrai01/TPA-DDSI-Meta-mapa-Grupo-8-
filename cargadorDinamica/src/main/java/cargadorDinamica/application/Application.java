package cargadorDinamica.application;

//import cargadorDinamica.configs.ApiCargadorDinamicoConfig;
//import cargadorDinamica.handlers.GetHechosProxyHandler;
//import cargadorDinamica.repository.DinamicaRepository;
        //handlers.PostFuenteHandler;
import cargadorDinamica.model.CargadorDinamico;
import cargadorDinamica.model.HechoAIntegrarDTO;
import cargadorDinamica.repository.DinamicaRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@SpringBootApplication
@RestController
@RequestMapping("/fuentesDinamicas")
public class Application {

    private final CargadorDinamico cargadorDinamico;
    private final DinamicaRepository dinamicaRepository;

    // Constructor para inyección de dependencias
    public Application() {
        this.cargadorDinamico = CargadorDinamico.getInstance();
        this.dinamicaRepository = DinamicaRepository.getInstance();
    }

    public static void main(String[] args) {
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

    //Hay que hacer el post del hecho de UI
    @PostMapping("/agregarHecho")
    public ResponseEntity<?> agregarHecho(@RequestBody HechoAIntegrarDTO hecho) {
        dinamicaRepository.add(hecho);
        return ResponseEntity.status(201).body("Fuente guardada correctamente");
    }
}
