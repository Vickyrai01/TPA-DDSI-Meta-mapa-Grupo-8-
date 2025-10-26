package cargadorDinamica.application;

import cargadorDinamica.model.CargadorDinamico;
import cargadorDinamica.model.HechoAIntegrarDTO;
import cargadorDinamica.repository.DinamicaRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utils.DBUtils;

import javax.persistence.EntityManager;
import java.util.List;


@SpringBootApplication
@RestController
@RequestMapping("/cargadorDinamico")
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

        EntityManager em = DBUtils.getEntityManager();
        DBUtils.comenzarTransaccion(em);

        HechoAIntegrarDTO hecho1 = new HechoAIntegrarDTO();
        hecho1.setHash("shfkjdshgjkhdfkjghdfkgh");
        hecho1.setDescripcion("de pruebaaaaa");
        hecho1.setTitulo("hecho 1");
        em.persist(hecho1);

        DBUtils.commit(em);


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
                hecho.getFechaSuceso()
        );

        dinamicaRepository.save(hechoDTO);
        return ResponseEntity.status(201).body("Hecho agregado correctamente");
    }

}
