package core.models.entities.colecciones.criterios;

import core.models.entities.hecho.Hecho;

import java.time.LocalDate;

public class CriterioFechaSuceso implements Criterio {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public CriterioFechaSuceso(LocalDate fechaInicio, LocalDate fechaFin){
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    @Override
    public boolean cumpleCriterio(Hecho hecho){
        return hecho.getFechaSuceso().isBefore(fechaFin) && hecho.getFechaSuceso().isAfter(fechaInicio);
    }
}
