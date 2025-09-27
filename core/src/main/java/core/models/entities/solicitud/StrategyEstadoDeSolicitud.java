package core.models.entities.solicitud;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import core.models.entities.hecho.Hecho;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = StrategySolicitudAceptada.class, name = "aceptada"),
        @JsonSubTypes.Type(value = StrategySolicitudPendiente.class, name = "pendiente"),
        @JsonSubTypes.Type(value = StrategySolicitudRechazada.class, name = "rechazada"),
})

public abstract class StrategyEstadoDeSolicitud {

    public StrategyEstadoDeSolicitud(){
    }

    public abstract void aceptarSolicitud(Hecho hecho);
    public abstract void rechazarSolicitud(Hecho hecho);
    public abstract String devolverTipoDeEstado();
}
