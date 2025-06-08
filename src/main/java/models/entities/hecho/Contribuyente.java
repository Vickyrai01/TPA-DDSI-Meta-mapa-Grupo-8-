package models.entities.hecho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
public class Contribuyente {
    @Setter
    @Getter
    private String nombre;
    @Setter
    @Getter
    private String apellido;
    @Setter
    @Getter
    private LocalDate fechaNacimiento;

}
