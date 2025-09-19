package core.models.entities.hecho;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class SugerenciaDeCambio {
    private String detalle;
    public String getDetalle() {
        return detalle;
    }
    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    private LocalDateTime fecha;
    public LocalDateTime getFecha() {
        return fecha;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public SugerenciaDeCambio(String detalle) {
        this.detalle = detalle;
        this.fecha = LocalDateTime.now();
    }

}
