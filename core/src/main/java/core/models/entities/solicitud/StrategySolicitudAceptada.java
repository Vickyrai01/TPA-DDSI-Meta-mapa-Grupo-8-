package core.models.entities.solicitud;

import core.models.entities.hecho.Hecho;

public class StrategySolicitudAceptada extends StrategyEstadoDeSolicitud {
    public StrategySolicitudAceptada() {

    }

    public void aceptarSolicitud(Hecho hecho) {}
    public void rechazarSolicitud(Hecho hecho) {}
    public String devolverTipoDeEstado(){
        return "ACEPTADA";
    }
}
