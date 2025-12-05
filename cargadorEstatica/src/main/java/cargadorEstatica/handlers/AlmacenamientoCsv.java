package cargadorEstatica.handlers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class AlmacenamientoCsv {
    private static final String BASE_CSV_DIR = "cargadorEstatica/csv"; // o "csv" según tu estructura

    public static String guardarCsv(Integer fuenteId, byte[] contenido, String nombreOriginal) throws IOException {
        Path carpeta = Paths.get(BASE_CSV_DIR);
        Files.createDirectories(carpeta);

        String nombreArchivo = (nombreOriginal == null || nombreOriginal.isBlank())
                ? "fuente_" + fuenteId + ".csv"
                : nombreOriginal;

        Path destino = carpeta.resolve(nombreArchivo);
        Files.write(destino, contenido, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        // devolvemos el nombre relativo que usará StrategyCSV
        return nombreArchivo; // p.ej. "fuente_3.csv"
    }

    public static void eliminarCsv(String nombreArchivo) throws IOException {
        Path archivo = Paths.get(BASE_CSV_DIR, nombreArchivo);
        Files.deleteIfExists(archivo);
    }
}
