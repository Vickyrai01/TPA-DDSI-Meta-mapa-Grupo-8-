package models.entities.fuentes;

import com.opencsv.CSVReader;
import models.entities.hecho.*;

import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSV {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static Map<String, Hecho> leerHechosDesdeCSV(String archivoCSV) {
        Map<String, Hecho> hechos = new HashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(archivoCSV))) {
            String[] fila;
            reader.readNext(); // Saltar encabezado

            while ((fila = reader.readNext()) != null) {
                if (fila.length != 6) {
                    System.out.println("Línea ignorada: no tiene 6 columnas.");
                    continue;
                }

                String titulo = fila[0].trim();
                String descripcion = fila[1].trim();
                String categoria = fila[2].trim();
                Double latitud = Double.valueOf(fila[3].trim());
                Double longitud = Double.valueOf(fila[4].trim());
                LocalDate fecha = LocalDate.parse(fila[5].trim(), FORMATO_FECHA);

                Coordenadas coordenadas = new Coordenadas(latitud, longitud);
                EtiquetaLugar etiquetaLugar = new EtiquetaLugar(coordenadas);
                EtiquetaCategoria etiquetaCategoria = new EtiquetaCategoria(categoria);
                EtiquetaFecha etiquetaFecha = new EtiquetaFecha(fecha);
                List<Etiqueta> etiquetaList = new ArrayList<>();
                etiquetaList.add(etiquetaCategoria);
                etiquetaList.add(etiquetaFecha);
                etiquetaList.add(etiquetaLugar);


    //VER LO DE TIPO DE FUENTE.
                Hecho hecho = new Hecho(titulo, descripcion, etiquetaList,
                        null, LocalDate.now(), null,
                        Estado.ACEPTADO, null, null, null);
                hechos.put(titulo, hecho);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hechos;
    }
}