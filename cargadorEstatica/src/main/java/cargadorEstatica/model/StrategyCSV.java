package cargadorEstatica.model;

import com.opencsv.CSVReader;

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

    public StrategyCSV() {}

    private static final String BASE_CSV_DIR = Paths.get("cargadorEstatica", "csv").toString();; // carpeta base

    @Override
    public List<HechoAIntegrarDTO> extraerHechosRecientes(String fuenteBase, String codigoFuente) {
        List<HechoAIntegrarDTO> hechos = new ArrayList<>();

        try {
            Path csvPath = Paths.get(BASE_CSV_DIR, fuenteBase);

            if (!Files.exists(csvPath)) {
                System.err.println("[StrategyCSV] No se encontró el archivo CSV: " + csvPath.toAbsolutePath());
                return List.of();
            }

            try (CSVReader reader = new CSVReader(
                    Files.newBufferedReader(csvPath, StandardCharsets.UTF_8))) { //<- esto por ahi rompe, ni idea que es guadi
                String[] fila;
                reader.readNext(); // header

                while ((fila = reader.readNext()) != null) {
                    if (fila.length != 6) {
                        System.out.println("Línea ignorada: no tiene 6 columnas." + Arrays.toString(fila));
                        continue;
                    }

                    if (Arrays.stream(fila).anyMatch(col -> col == null || col.trim().isEmpty())) {
                        System.out.println("Fila ignorada: tiene campos vacíos. " + Arrays.toString(fila));
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
                System.err.println("[StrategyCSV] Error al leer CSV: " + fuenteBase);
                e.printStackTrace();
        }catch (Exception e) {
                System.err.println("[StrategyCSV] Error inesperado procesando CSV: " + fuenteBase);
                e.printStackTrace();
        }
        return hechos.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public String devolverTipoDeConexion() {
        return "CSV";
    }
}

