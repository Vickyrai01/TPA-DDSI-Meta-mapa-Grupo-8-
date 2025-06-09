package models.entities.fuentes;

import com.opencsv.CSVReader;
import models.entities.hecho.Hecho;

import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class StrategyCSV implements StrategyTipoConexion {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public Map<String, Hecho> agregarHecho(String FuenteBase) {

        Map<String, Hecho> hechos = new HashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(FuenteBase))) {
            String[] fila;
            reader.readNext(); // Saltar encabezado

            while ((fila = reader.readNext()) != null) {
                if (fila.length != 6) {
                    System.out.println("Línea ignorada: no tiene 6 columnas.");
                    continue;
                }

                String titulo = fila[0].trim();
                String descripcion = fila[1].trim();
                LocalDate fecha = LocalDate.parse(fila[5].trim(), FORMATO_FECHA);

                Hecho hecho = new Hecho(titulo, descripcion, fecha);
                hechos.put(titulo, hecho);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hechos;
    }

    }
