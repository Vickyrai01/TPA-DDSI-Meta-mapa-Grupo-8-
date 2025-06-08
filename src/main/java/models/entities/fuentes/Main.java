package models.entities.fuentes;

import models.entities.hecho.Hecho;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        String ruta = "desastres_sanitarios_contaminacion_argentina.csv"; // Reemplaza con la ruta real del archivo

        File archivo = new File(ruta);
        if (!archivo.exists()) {
            System.out.println("Archivo no encontrado: " + ruta);
            return;
        }
        else {System.out.println("se encontro el archivo");}

        Map<String, Hecho> todosLosHechos = CSV.leerHechosDesdeCSV(ruta);

        // Verifica si hay hechos cargados
        if (todosLosHechos.isEmpty()) {
            System.out.println("No se cargaron hechos. Verifica el archivo CSV.");
            return;
        }

        // Crear un nuevo mapa con solo los primeros 10
        Map<String, Hecho> primeros10 = new LinkedHashMap<>();

        Iterator<Map.Entry<String, Hecho>> iterator = todosLosHechos.entrySet().iterator();
        int contador = 0;

        while (iterator.hasNext() && contador < 10) {
            Map.Entry<String, Hecho> entrada = iterator.next();
            primeros10.put(entrada.getKey(), entrada.getValue());
            contador++;
        }

        // Mostrar los 10 primeros hechos
        System.out.println("Primeros 10 hechos:");
        for (Hecho hecho : primeros10.values()) {
            System.out.println(hecho.toString());
        }
    }
}

