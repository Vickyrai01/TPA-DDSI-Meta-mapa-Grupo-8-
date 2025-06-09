package models.entities.fuentes;

import models.entities.hecho.Hecho;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class CSVTest {
    public static void main(String[] args) {

        StrategyCSV strategy = new StrategyCSV();

        String ruta = "desastres_sanitarios_contaminacion_argentina.csv";

        System.out.println("*********DEMO CSV: SOLO MUESTRA LOS PRIMEROS 15**************");
        System.out.println("Buscando archivo...");
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            System.out.println("...Archivo no encontrado: " + ruta);
            return;
        }
        else {System.out.println("...¡Se encontro el archivo!");}
        System.out.println( "                          ");

        Map<String, Hecho> todosLosHechos = strategy.agregarHecho(ruta);

        if (todosLosHechos.isEmpty()) {
            System.out.println("ERROR: No se cargaron hechos. Verifica el archivo CSV.");
            return;
        }

        Map<String, Hecho> primeros15 = new LinkedHashMap<>();

        Iterator<Map.Entry<String, Hecho>> iterator = todosLosHechos.entrySet().iterator();
        int contador = 0;

        while (iterator.hasNext() && contador < 15) {
            Map.Entry<String, Hecho> entrada = iterator.next();
            primeros15.put(entrada.getKey(), entrada.getValue());
            contador++;
        }

        System.out.println("Primeros 15 hechos encontrados:");
        for (Hecho hecho : primeros15.values()) {
            System.out.println(hecho.toString());
        }
    }
}
