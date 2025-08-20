package models.entities.normalizador;

import models.entities.fuentes.TipoFuente;
import models.entities.hecho.*;

import java.time.LocalDate;
import java.util.List;

    //Esto va a pasar un hecho DTO a un hecho
public class FactoryHecho {
    NormalizadorFecha normalizadorFecha = NormalizadorFecha.getInstance();
    NormalizadorCategoria normalizadorCategoria = NormalizadorCategoria.getInstance();
    
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
                TipoFuente.ESTATICA, //Por el momento creo que es la única,
                null,
                hecho.getDescripcion(),
                hecho.getTitulo(),
                null //poner la logica
        );
    }

}
