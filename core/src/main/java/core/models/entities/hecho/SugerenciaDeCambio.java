package core.models.entities.hecho;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "sugerenciaDeCambio")
public class SugerenciaDeCambio {
    @Id
    @Column(name = "id_sugerenciaDeCambio")
    private Integer id;
    @Column(name = "detalle")
    private String detalle;
    public String getDetalle() {
        return detalle;
    }
    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    @Column(name = "fecha")
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

    public SugerenciaDeCambio(){}
}
