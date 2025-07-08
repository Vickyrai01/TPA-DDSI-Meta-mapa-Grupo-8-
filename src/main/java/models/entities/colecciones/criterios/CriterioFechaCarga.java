package models.entities.colecciones.criterios;

import java.time.LocalDate;

public class CriterioFechaCarga implements Criterio{
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @Override
    public boolean cumpleCriterio(models.entities.hecho.Hecho hecho) {
        return hecho.getFechaCarga().isBefore(fechaFin) && hecho.getFechaCarga().isAfter(fechaInicio);
    }
}
