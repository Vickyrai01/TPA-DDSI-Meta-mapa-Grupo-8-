package core.models.entities.colecciones.criterios;

import core.models.entities.hecho.Hecho;

import java.time.LocalDate;
import javax.persistence.*;
@Entity
@DiscriminatorValue("FECHA_CARGA")
public class CriterioFechaCarga extends Criterio {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public CriterioFechaCarga(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public CriterioFechaCarga() {
    }
    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        return hecho.getFechaCarga().isBefore(fechaFin) && hecho.getFechaCarga().isAfter(fechaInicio);
    }
}
