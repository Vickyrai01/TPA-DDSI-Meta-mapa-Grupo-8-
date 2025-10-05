package core.models.entities.solicitud;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StrategyEstadoDeSolicitudConverter implements AttributeConverter<StrategyEstadoDeSolicitud, String> {
    @Override
    public String convertToDatabaseColumn(StrategyEstadoDeSolicitud strategy) {
        if (strategy == null) {
            return null;
        }
        return strategy.devolverTipoDeEstado();
    }

    @Override
    public StrategyEstadoDeSolicitud convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return switch (dbData) {
            case "ACEPTADA" -> new StrategySolicitudAceptada();
            case "PENDIENTE" -> new StrategySolicitudPendiente();
            case "RECHAZADA" -> new StrategySolicitudRechazada();
            default -> throw new IllegalArgumentException("Tipo de estrategia desconocido: " + dbData);

        };
    }
}
