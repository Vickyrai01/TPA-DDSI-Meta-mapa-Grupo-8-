package models.entities.hecho;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class SugerenciaDeCambio {
    @Setter
    @Getter
    private String detalle;

    @Setter
    @Getter
    private LocalDateTime fecha;

    public SugerenciaDeCambio(String detalle) {
        this.detalle = detalle;
        this.fecha = LocalDateTime.now();
    }

}
