package core.models.entities.solicitud;

import core.models.entities.hecho.Hecho;
    public class StrategySolicitudPendiente extends StrategyEstadoDeSolicitud {

        public StrategySolicitudPendiente() {

        }

        public void aceptarSolicitud(Hecho hecho) {
            hecho.desactivarse();
        }

        public void rechazarSolicitud(Hecho hecho) {
        }

        public String devolverTipoDeEstado(){
            return "PENDIENTE";
        }
}
