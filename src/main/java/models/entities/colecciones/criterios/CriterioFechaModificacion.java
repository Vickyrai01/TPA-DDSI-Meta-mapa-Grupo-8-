package models.entities.colecciones.criterios;

import models.entities.hecho.Hecho;

import java.time.LocalDate;

public class CriterioFechaModificacion implements Criterio{
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @Override
    public boolean cumpleCriterio(Hecho hecho){
        return hecho.getUltimaFechaModificacion().isBefore(fechaFin) && hecho.getUltimaFechaModificacion().isAfter(fechaInicio);

    }
}
