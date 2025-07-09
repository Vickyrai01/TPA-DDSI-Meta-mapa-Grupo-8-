package models.entities.solicitud;

public abstract class StrategyEstadoDeSolicitud {
    private SolicitudDeEliminacion solicitud;

    public StrategyEstadoDeSolicitud(SolicitudDeEliminacion solicitudDeEliminacion){
        solicitud = solicitudDeEliminacion;
    }

    public abstract void aceptarSolicitud();
    public abstract void rechazarSolicitud();
}
