package csv;

import servicioEstadisticas.model.ServicioEstadisticas;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneradorDeEstadisticas {
    GeneradorCSV csvGenerator = GeneradorCSV.getInstance();
    private static ServicioEstadisticas servicioEstadisticas = ServicioEstadisticas.getInstance();

    public void generarEstadisticas(){
        List<String> headers = new ArrayList<>();
        headers.add("provincia");
        headers.add("cantidadHechos");

        List<Map<String, String>> datos = List.of(
                Map.of("provincia", "Corrientes",  "cantidadHechos", "3"),
                Map.of("provincia", "Entre Rios",  "cantidadHechos", "2"),
                Map.of("provincia", "Misiones",    "cantidadHechos", "1")
        );

        try {
            csvGenerator.writeCsv("PRUEBA", datos, headers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
//(String dir,
//                                String fileName,
//                                List<Map<String, String>> rows,
//                                List<String> columns)