package models.entities.normalizador;

import models.entities.fuentes.TipoFuente;
import models.entities.hecho.*;
import models.repository.CategoriaRepository;

import java.time.LocalDate;
import java.util.List;

    //Esto va a pasar un hecho DTO a un hecho
public class FactoryHecho {
     private static volatile FactoryHecho instance;

        public FactoryHecho() {
            if (instance != null) {
                throw new RuntimeException("Usa getInstance() para obtener el Singleton");
            }
        }

        public static FactoryHecho getInstance() {
            if (instance == null) {
                synchronized (FactoryHecho.class) {
                    if (instance == null) {
                        instance = new FactoryHecho();
                    }
                }
            }
            return instance;
        }

    public Hecho convertirHecho(HechoAIntegrarDTO hecho, LocalDate fecha, Categoria categoria){
        // Convertir Coordenadas
        double latitud = Double.parseDouble(hecho.getLatitud());
        double longitud = Double.parseDouble(hecho.getLongitud());
        Coordenadas ubicacion = new Coordenadas(latitud, longitud);

        return new Hecho(
                999, //Analizar como asignar el ID
                ubicacion,
                categoria,
                null,
                LocalDate.now(),
                null,
                Estado.ACEPTADO,
                null,
                LocalDate.now(),
                fecha,
                TipoFuente.ESTATICA, //Evaluar
                null,
                hecho.getDescripcion(),
                hecho.getTitulo(),
                null //poner la logica
        );
    }

}
