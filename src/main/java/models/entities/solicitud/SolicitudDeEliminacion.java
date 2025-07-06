package models.entities.solicitud;

import lombok.Getter;
import lombok.Setter;
import models.entities.hecho.Hecho;
import models.entities.hecho.Estado;
import models.entities.solicitud.DetectorDeSpam;
import java.time.LocalDateTime;

public class SolicitudDeEliminacion implements DetectorDeSpam {

    private Hecho hecho;

    private String descripcion;

    private Boolean aceptada;

    private LocalDateTime fechaDeRevision;

    public SolicitudDeEliminacion(Hecho hecho, String descripcion, Boolean aceptada, LocalDateTime fechaDeRevision) {
        this.hecho = hecho;
        this.descripcion = descripcion;
        this.aceptada = aceptada;
        this.fechaDeRevision = fechaDeRevision;
    }

    public Hecho getHecho() {
        return hecho;
    }

    public void setHecho(Hecho hecho) {
        this.hecho = hecho;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getAceptada() {
        return aceptada;
    }

    public void setAceptada(Boolean aceptada) {
        this.aceptada = aceptada;
    }

    public LocalDateTime getFechaDeRevision() {
        return fechaDeRevision;
    }

    public void setFechaDeRevision(LocalDateTime fechaDeRevision) {
        this.fechaDeRevision = fechaDeRevision;
    }

    public void aceptarSolicitud(Hecho hecho) {
        this.aceptada = true;
        hecho.desactivarse();
    }

    public void rechazarSolicitud() {
        this.aceptada = false;
    }

    public void rechazarPorSpam() {
        if (esSpam(descripcion)) {
            this.rechazarSolicitud();
        }
    }
    
    public String toString() {
        return "SolicitudDeEliminacion{" +
                "hecho=" + hecho +
                ", descripcion='" + descripcion + '\'' +
                ", aceptada=" + aceptada +
                ", fechaDeRevision=" + fechaDeRevision +
                '}';
    }

}
