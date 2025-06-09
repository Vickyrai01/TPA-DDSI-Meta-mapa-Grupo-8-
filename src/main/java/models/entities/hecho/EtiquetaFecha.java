package models.entities.hecho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


public class EtiquetaFecha extends Etiqueta {
    @Setter
    @Getter
    private LocalDate fechaAcontecimiento;

    public EtiquetaFecha(LocalDate fechaAcontecimiento) {
        this.fechaAcontecimiento = fechaAcontecimiento;
    }

    @Override
    public void cambiarFecha(LocalDate fechaNueva) {
        this.setFechaAcontecimiento(fechaNueva);
    }

    public LocalDate getFechaAcontecimiento() {
        return fechaAcontecimiento;
    }

    public void setFechaAcontecimiento(LocalDate fechaAcontecimiento) {
        this.fechaAcontecimiento = fechaAcontecimiento;
    }
}
