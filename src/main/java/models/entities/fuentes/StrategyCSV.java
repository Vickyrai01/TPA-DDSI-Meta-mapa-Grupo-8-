package models.entities.fuentes;


import com.opencsv.CSVReader;
import models.entities.colecciones.criterios.Criterio;
import models.entities.hecho.*;
import models.entities.hecho.Hecho;

import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.*;
import java.util.HashMap;
import java.util.stream.Collectors;

public class StrategyCSV implements StrategyTipoConexion {


    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public List<Hecho> extraerHecho(List<Criterio> criterio, String fuenteBase) {
        Map<String, Hecho> hechos = new HashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(fuenteBase))) {
            String[] fila;
            reader.readNext();

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
                Categoria categoria1 = new Categoria(categoria);


                Hecho hecho = new Hecho(7, coordenadas, categoria1, null, null,
                        null, Estado.ACEPTADO, null, LocalDate.now(),
                        fecha, TipoFuente.ESTATICA, null, descripcion, titulo);
                hechos.put(titulo, hecho);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Hecho> listaHechos = hechos.values()
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return listaHechos;


    }


    @Override
    public List<Hecho> agregarHecho(String fuenteBase,  Hecho hecho) { return null;
    }
}

