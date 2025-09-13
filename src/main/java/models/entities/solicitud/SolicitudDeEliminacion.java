package models.entities.solicitud;

import models.agregador.DetectorDeSpam;
import models.entities.hecho.Hecho;

import java.time.LocalDateTime;

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

    private StateEstadoDeSolicitud stateEstadoDeSolicitud = new StateSolicitudPendiente(this) {
    };

    public StateEstadoDeSolicitud getStrategyEstadoDeSolicitud() {
        return stateEstadoDeSolicitud;
    }
    public void setStateEstadoDeSolicitud(StateEstadoDeSolicitud stateEstadoDeSolicitud) {
        this.stateEstadoDeSolicitud = stateEstadoDeSolicitud;
    }

    public void aceptarSolicitud() {
        stateEstadoDeSolicitud.aceptarSolicitud(hecho);
    }

    public void rechazarSolicitud() {
        stateEstadoDeSolicitud.rechazarSolicitud(hecho);
    }

    public DetectorDeSpam detectorDeSpam = new DetectorDeSpam();

    public void revisarPorSpam() {
        if (detectorDeSpam.esSpam(descripcion)) {
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

    public void aceptada(){
        this.setAceptada(true);
        this.setFechaDeRevision(LocalDateTime.now());
        this.setStateEstadoDeSolicitud(new StateSolicitudAceptada(this));
    }

    public void rechazada(){
        this.setAceptada(false);
        this.setFechaDeRevision(LocalDateTime.now());
        this.setStateEstadoDeSolicitud(new StateSolicitudRechazada(this));
    }

}
