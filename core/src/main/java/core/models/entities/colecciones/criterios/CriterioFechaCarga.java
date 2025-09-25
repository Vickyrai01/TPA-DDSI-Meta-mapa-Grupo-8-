package core.models.entities.colecciones.criterios;

import core.models.entities.hecho.Hecho;

import java.time.LocalDate;

public class CriterioFechaCarga implements Criterio{
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public CriterioFechaCarga(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        return hecho.getFechaCarga().isBefore(fechaFin) && hecho.getFechaCarga().isAfter(fechaInicio);
    }
}
