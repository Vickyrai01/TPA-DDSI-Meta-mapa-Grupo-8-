package models.entities.fuentes;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import models.entities.colecciones.criterios.*;
import models.entities.hecho.Hecho;
import models.entities.normalizador.HechoAIntegrarDTO;

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CriterioCategoria.class, name = "categoria"),
        @JsonSubTypes.Type(value = CriterioDescripcion.class, name = "descripcion"),
        @JsonSubTypes.Type(value = CriterioFechaCarga.class, name = "fechaCarga"),
        @JsonSubTypes.Type(value = CriterioUbicacion.class, name = "ubicacion"),
        @JsonSubTypes.Type(value = CriterioFechaModificacion.class, name = "fechaModificacion"),
        @JsonSubTypes.Type(value = CriterioFechaSuceso.class, name = "fechaSuceso"),
        @JsonSubTypes.Type(value = CriterioNombre.class, name = "nombre"),
        @JsonSubTypes.Type(value = CriterioFechaModificacion.class, name = "fechaModificacion"),
})
public interface StrategyTipoConexion {

    public List<HechoAIntegrarDTO> extraerHecho(List<Criterio> criterio, String fuente, String codigoFuente);

    public List<HechoAIntegrarDTO> extraerHechosRecientes(String fuente, String codigoFuente);
}
