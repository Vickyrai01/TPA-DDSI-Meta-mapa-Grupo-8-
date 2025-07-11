package models.entities.solicitud;

import models.entities.hecho.Hecho;
    public class StateSolicitudPendiente extends StateEstadoDeSolicitud{

        public StateSolicitudPendiente(SolicitudDeEliminacion solicitudDeEliminacion) {
            super(solicitudDeEliminacion);
        }

        public void aceptarSolicitud(Hecho hecho) {
            solicitud.aceptada();
            hecho.desactivarse();
        }

        public void rechazarSolicitud(Hecho hecho) {
            solicitud.rechazada();
        }
}
