package models.entities.fuentes;

import models.entities.hecho.Hecho;

import java.io.File;
import java.util.List;


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

        List<Hecho> todosLosHechos = strategy.extraerHecho(null,ruta);

        if (todosLosHechos.isEmpty()) {
            System.out.println("ERROR: No se cargaron hechos. Verifica el archivo CSV.");
            return;
        }

        List<Hecho> primeros15 = todosLosHechos.stream().limit(15).toList();

        System.out.println("Primeros 15 hechos encontrados:");
        primeros15.forEach(System.out::println);
    }
}
