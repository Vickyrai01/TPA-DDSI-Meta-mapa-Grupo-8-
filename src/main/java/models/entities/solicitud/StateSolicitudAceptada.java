package models.entities.solicitud;

import models.entities.hecho.Hecho;

public class StateSolicitudAceptada extends StateEstadoDeSolicitud{
    public StateSolicitudAceptada(SolicitudDeEliminacion solicitudDeEliminacion) {
        super(solicitudDeEliminacion);
    }

    public void aceptarSolicitud(Hecho hecho) {}
    public void rechazarSolicitud(Hecho hecho) {}
}
