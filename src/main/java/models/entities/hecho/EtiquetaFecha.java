package models.entities.hecho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
public class EtiquetaFecha extends Etiqueta {
    @Setter
    @Getter
    private LocalDate fechaAcontecimiento;

    @Override
    public void cambiarFecha(LocalDate fechaNueva) {
        this.setFechaAcontecimiento(fechaNueva);
    }
}
