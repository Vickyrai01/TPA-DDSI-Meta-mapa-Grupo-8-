package core.models.entities.solicitud;

import core.models.entities.hecho.Hecho;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity(name = "solicitud_de_eliminacion")
public class SolicitudDeEliminacion {

    public SolicitudDeEliminacion(Integer id, Hecho hecho, String descripcion, Boolean aceptada, LocalDateTime fechaDeRevision) {
        this.id = id;
        this.hecho = hecho;
        this.descripcion = descripcion;
        this.aceptada = aceptada;
        this.fechaDeRevision = fechaDeRevision;
    }

    public SolicitudDeEliminacion(Integer id, Hecho hecho, String descripcion) {
        this.id = id;
        this.hecho = hecho;
        this.descripcion = descripcion;
        this.aceptada = null;
        this.fechaDeRevision = null;
    }

    public SolicitudDeEliminacion() {}

    @Id
    @Column(name = "id")
    private Integer id;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne
    private Hecho hecho;
    public Hecho getHecho() {
        return hecho;
    }
    public void setHecho(Hecho hecho) {
        this.hecho = hecho;
    }

    @Column(name = "descripcion")
    private String descripcion;
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Column(name = "aceptada")
    private Boolean aceptada;
    public Boolean getAceptada() {
        return aceptada;
    }
    public void setAceptada(Boolean aceptada) {
        this.aceptada = aceptada;
    }

    @Column(name = "fechaDeRevision")
    private LocalDateTime fechaDeRevision;
    public LocalDateTime getFechaDeRevision() {
        return fechaDeRevision;
    }
    public void setFechaDeRevision(LocalDateTime fechaDeRevision) {
        this.fechaDeRevision = fechaDeRevision;
    }


    public void aceptarSolicitud() {
        if(aceptada != true) {
            this.aceptada();
        }
    }

    public void rechazarSolicitud() {
        this.rechazada();
    }

    public String toString() {
        return "solicitud_de_eliminacion{" +
                "hecho=" + hecho +
                ", descripcion='" + descripcion + '\'' +
                ", aceptada=" + aceptada +
                ", fechaDeRevision=" + fechaDeRevision +
                '}';
    }

    public void aceptada(){
        this.setAceptada(true);
        this.setFechaDeRevision(LocalDateTime.now());
        hecho.desactivarse();
    }

    public void rechazada(){
        hecho.activarse();
        this.setAceptada(false);
        this.setFechaDeRevision(LocalDateTime.now());
    }

}
