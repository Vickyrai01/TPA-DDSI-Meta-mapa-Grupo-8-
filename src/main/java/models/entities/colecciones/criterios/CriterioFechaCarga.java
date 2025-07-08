package models.entities.colecciones.criterios;

import java.time.LocalDate;

public class CriterioFechaCarga implements Criterio{
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public CriterioFechaCarga(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    @Override
    public boolean cumpleCriterio(models.entities.hecho.Hecho hecho) {
        return hecho.getFechaCarga().isBefore(fechaFin) && hecho.getFechaCarga().isAfter(fechaInicio);
    }
}
