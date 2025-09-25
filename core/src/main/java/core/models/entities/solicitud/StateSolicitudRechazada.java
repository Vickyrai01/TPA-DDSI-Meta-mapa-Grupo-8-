package core.models.entities.solicitud;

import core.models.entities.hecho.Hecho;

public class StateSolicitudRechazada extends StateEstadoDeSolicitud{
    public StateSolicitudRechazada(SolicitudDeEliminacion solicitudDeEliminacion) {
        super(solicitudDeEliminacion);
    }

    public void aceptarSolicitud(Hecho hecho) {}
    public void rechazarSolicitud(Hecho hecho) {}
}
