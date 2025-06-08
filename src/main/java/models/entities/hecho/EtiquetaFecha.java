package models.entities.hecho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
public class EtiquetaFecha extends Etiqueta {
    @Setter
    @Getter
    private LocalDateTime fechaAcontecimiento;
}
