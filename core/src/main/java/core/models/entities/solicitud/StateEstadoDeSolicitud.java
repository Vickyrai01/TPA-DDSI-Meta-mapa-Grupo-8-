package core.models.entities.solicitud;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import core.models.entities.hecho.Hecho;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = StateSolicitudAceptada.class, name = "aceptada"),
        @JsonSubTypes.Type(value = StateSolicitudPendiente.class, name = "pendiente"),
        @JsonSubTypes.Type(value = StateSolicitudRechazada.class, name = "rechazada"),
})

public abstract class StateEstadoDeSolicitud {
    SolicitudDeEliminacion solicitud;

    public StateEstadoDeSolicitud(SolicitudDeEliminacion solicitudDeEliminacion){
        solicitud = solicitudDeEliminacion;
    }

    public abstract void aceptarSolicitud(Hecho hecho);
    public abstract void rechazarSolicitud(Hecho hecho);
}
