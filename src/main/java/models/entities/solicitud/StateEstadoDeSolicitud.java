package models.entities.solicitud;

import models.entities.hecho.Hecho;

public abstract class StateEstadoDeSolicitud {
    SolicitudDeEliminacion solicitud;

    public StateEstadoDeSolicitud(SolicitudDeEliminacion solicitudDeEliminacion){
        solicitud = solicitudDeEliminacion;
    }

    public abstract void aceptarSolicitud(Hecho hecho);
    public abstract void rechazarSolicitud(Hecho hecho);
}
