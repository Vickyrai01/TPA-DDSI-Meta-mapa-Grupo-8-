package cargadorEstatica.application;
import org.springframework.web.multipart.MultipartFile;

import cargadorEstatica.model.*;
import cargadorEstatica.repository.RepositoryFuentes;
import cargadorEstatica.repository.RepositoryFuentesSeeder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.*;
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
        Fuente fuente = new Fuente(fuenteDTO.getId(), fuenteDTO.getNombre(), fuenteDTO.getLink(), strategyFuente, fuenteDTO.getTipoFuente());
        repoFuentes.save(fuente);
        return ResponseEntity.status(201).build();
    }

    private StrategyTipoConexion obtenerStrategyFuente(String tipoFuente) {
        if (tipoFuente.equals("ESTATICA")) return new StrategyCSV();
        else return null;
    }

    @GetMapping("/obtenerFuentes")
    public ResponseEntity<List<Fuente>> obtenerFuentes(){
        List<Fuente> fuentes = repoFuentes.findAll();
        if(fuentes.isEmpty()) return ResponseEntity.status(204).build();
        return ResponseEntity.ok(fuentes);
    }

    @PostMapping("/fuentes/{id}/csv")
    public ResponseEntity<?> subirCsv(
            @PathVariable("id") Integer fuenteId,
            @RequestParam("archivoCsv") MultipartFile archivoCsv
    ) {
        try {
            var fuente = repoFuentes.findById(fuenteId);
            if (fuente == null) {
                return ResponseEntity.status(404).body("Fuente no encontrada");
            }

            Path carpeta = Paths.get("cargadorEstatica", "csv");
            Files.createDirectories(carpeta);

            String nombreArchivo = archivoCsv.getOriginalFilename();
            if (nombreArchivo == null || nombreArchivo.isBlank()) {
                nombreArchivo = "fuente_" + fuenteId + ".csv";
            }

            Path destino = carpeta.resolve(nombreArchivo);
            Files.copy(archivoCsv.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            // guardar solo el nombre relativo, que StrategyCSV usa
            fuente.setLink(nombreArchivo);
            repoFuentes.save(fuente);

            return ResponseEntity.status(201).body("CSV guardado correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al guardar CSV");
        }
    }

    @PostMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarFuente(@PathVariable("id") Integer id){
        try {
            Fuente fuente = repoFuentes.findById(id);
            if(fuente == null) return ResponseEntity.status(404).build();
            try {
                Path carpeta = Paths.get("cargadorEstatica", "csv");
                Path destino = carpeta.resolve(fuente.getLink());
                Files.deleteIfExists(destino);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("No se pudo borrar archivo físico");
            }
            repoFuentes.deleteById(id);
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("Error al eliminar fuente estática");
        }
    }
}

