package core.models.entities.solicitud;

import core.models.entities.hecho.Hecho;

public class StrategySolicitudRechazada extends StrategyEstadoDeSolicitud {
    public StrategySolicitudRechazada() {

    }

    public void aceptarSolicitud(Hecho hecho) {}
    public void rechazarSolicitud(Hecho hecho) {}
    public String devolverTipoDeEstado(){
        return "RECHAZADA";
    }
}
