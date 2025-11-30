package servicioEstadisticas.model;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hecho")
public class Hecho {

    @Id
    private String hash;

    @Column(name = "categoria")
    private String categoria;

    @Column(name= "fecha_suceso")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fecha_suceso;

    @Column(name= "provincia")
    private String provincia;

    @OneToOne
    @JoinColumn(name = "id_ubicacion")
    private Coordenadas coordenadas;


    public Hecho(String id, String categoria, LocalDateTime fecha_suceso, String provincia, Coordenadas coordenadas) {
        this.hash = id;
        this.categoria = categoria;
        this.fecha_suceso = fecha_suceso;
        this.provincia = provincia;
        this.coordenadas = coordenadas;
    }

    public Hecho() {}

    public String getHash() {
        return hash;
    }

    public void setHash(String id_hecho) {
        this.hash = hash;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDateTime getFecha_suceso() {
        return fecha_suceso;
    }

    public void setFecha_suceso(LocalDateTime fechaSuceso) {
        this.fecha_suceso = fechaSuceso;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public Coordenadas getCoordenadas() {return coordenadas;}

    public void setCoordenadas(Coordenadas coordenadas) {this.coordenadas = coordenadas;}

}
