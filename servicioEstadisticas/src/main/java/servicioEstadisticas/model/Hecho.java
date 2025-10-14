package servicioEstadisticas.model;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hecho")
public class Hecho {

    @Id
    private String id_hecho;

    @Column(name = "categoria")
    private String categoria;

    @Column(name= "fecha_suceso")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaSuceso;

    @Column(name= "provincia")
    private String provincia;


    public Hecho(String id,String categoria, LocalDateTime fechaSuceso, String provincia) {
        this.id_hecho = id;
        this.categoria = categoria;
        this.fechaSuceso = fechaSuceso;
        this.provincia = provincia;
    }

    public Hecho() {}

    public String getId_hecho() {
        return id_hecho;
    }

    public void setId_hecho(String id_hecho) {
        this.id_hecho = id_hecho;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDateTime getFechaSuceso() {
        return fechaSuceso;
    }

    public void setFechaSuceso(LocalDateTime fechaSuceso) {
        this.fechaSuceso = fechaSuceso;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

}
