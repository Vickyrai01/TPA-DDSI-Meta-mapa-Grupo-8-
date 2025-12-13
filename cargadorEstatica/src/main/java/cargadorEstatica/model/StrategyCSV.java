package cargadorEstatica.model;

import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StrategyCSV implements StrategyTipoConexion {

    private static final Logger log = LoggerFactory.getLogger(StrategyCSV.class);
    public StrategyCSV() {}

    private static final String BASE_CSV_DIR = Paths.get("cargadorEstatica", "csv").toString();; // carpeta base

    @Override
    public List<HechoAIntegrarDTO> extraerHechosRecientes(String fuenteBase, String codigoFuente) {
        List<HechoAIntegrarDTO> hechos = new ArrayList<>();

        try {
            Path csvPath = Paths.get(BASE_CSV_DIR, fuenteBase);
            log.info("Procesando CSV fuente={} path={}", fuenteBase, csvPath.toAbsolutePath());
            if (!Files.exists(csvPath)) {
                log.warn("No se encontró el archivo CSV path={}", csvPath.toAbsolutePath());
                return List.of();
            }

            try (CSVReader reader = new CSVReader(
                    Files.newBufferedReader(csvPath, StandardCharsets.UTF_8))) {
                String[] fila;
                reader.readNext(); // header

                while ((fila = reader.readNext()) != null) {
                    if (fila.length != 6) {
                        continue;
                    }

                    if (Arrays.stream(fila).anyMatch(col -> col == null || col.trim().isEmpty())) {
                        continue;
                    }

                    String titulo = fila[0].trim();
                    String descripcion = fila[1].trim();
                    String categoria = fila[2].trim();
                    String latitud = fila[3].trim();
                    String longitud = fila[4].trim();
                    String fecha = fila[5].trim();

                    HechoAIntegrarDTO hechoAIntegrarDTO = new HechoAIntegrarDTO(
                            titulo, descripcion, categoria, latitud, longitud, fecha
                    );
                    hechos.add(hechoAIntegrarDTO);
                }
            }
            }catch (IOException e) {
            log.error("Error al leer CSV fuente={}", fuenteBase, e);
        }catch (Exception e) {
            log.error("Error inesperado procesando CSV fuente={}", fuenteBase, e);
        }
        log.info("CSV procesado fuente={} hechosGenerados={}", fuenteBase, hechos.size());
        return hechos.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public String devolverTipoDeConexion() {
        return "CSV";
    }
}

