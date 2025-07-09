package models.entities.solicitud;

import io.javalin.http.Handler;
import lombok.Getter;
import lombok.Setter;
import models.entities.hecho.Hecho;
import models.entities.hecho.Estado;
import models.entities.solicitud.DetectorDeSpam;
import java.time.LocalDateTime;

public class SolicitudDeEliminacion implements DetectorDeSpam {
    public SolicitudDeEliminacion(Integer id, Hecho hecho, String descripcion, Boolean aceptada, LocalDateTime fechaDeRevision) {
        this.id = id;
        this.hecho = hecho;
        this.descripcion = descripcion;
        this.aceptada = aceptada;
        this.fechaDeRevision = fechaDeRevision;
    }
    public SolicitudDeEliminacion() {}

    private Integer id;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    private Hecho hecho;
    public Hecho getHecho() {
        return hecho;
    }
    public void setHecho(Hecho hecho) {
        this.hecho = hecho;
    }

    private String descripcion;
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    private Boolean aceptada;
    public Boolean getAceptada() {
        return aceptada;
    }
    public void setAceptada(Boolean aceptada) {
        this.aceptada = aceptada;
    }

    private LocalDateTime fechaDeRevision;
    public LocalDateTime getFechaDeRevision() {
        return fechaDeRevision;
    }
    public void setFechaDeRevision(LocalDateTime fechaDeRevision) {
        this.fechaDeRevision = fechaDeRevision;
    }

    private StrategyEstadoDeSolicitud strategyEstadoDeSolicitud = new StrategyEstadoDeSolicitud(this) {
    };
    public StrategyEstadoDeSolicitud getStrategyEstadoDeSolicitud() {
        return strategyEstadoDeSolicitud;
    }
    public void setStrategyEstadoDeSolicitud(StrategyEstadoDeSolicitud strategyEstadoDeSolicitud) {
        this.strategyEstadoDeSolicitud = strategyEstadoDeSolicitud;
    }


    public void aceptarSolicitud(Hecho hecho) {
        strategyEstadoDeSolicitud.aceptarSolicitud(hecho, this);
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
