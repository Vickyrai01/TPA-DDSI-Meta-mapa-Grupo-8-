package csv;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GeneradorDeEstadisticas {
    GeneradorCSV csvGenerator = new GeneradorCSV();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    String timestamp = LocalDateTime.now().format(formatter);

    String path = "servicioEstadisticas/estadisticasGeneradas/estadisticas" + timestamp + ".csv";

    public void generarEstadisticas(){
        String[] headers = { "id", "provincia", "cantidadHechos" };

        List<String[]> rows = List.of(
                new String[]{ "1", "CABA", "30798" },
                new String[]{ "2", "Córdoba", "23324" },
                new String[]{ "3", "San Juan", "28123" }
        );

        try {
            csvGenerator.writeCsv(path, rows, headers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
