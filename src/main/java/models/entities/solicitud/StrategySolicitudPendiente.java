package models.entities.solicitud;

import models.entities.hecho.Hecho;
    public class StrategySolicitudPendiente {

    public void aceptarSolicitud(Hecho hecho) {

    }

    public void rechazarSolicitud() {
        this.aceptada = false;
    }
}
