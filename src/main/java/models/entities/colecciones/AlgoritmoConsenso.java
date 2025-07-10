package models.entities.colecciones;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import models.entities.hecho.Hecho;
import models.entities.fuentes.Fuente;

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = StrategyAbsoluta.class, name = "absoluto"),
        @JsonSubTypes.Type(value = StrategyMayoriaSimple.class, name = "mayoria simple"),
        @JsonSubTypes.Type(value = StrategyMultiplesMenciones.class, name = "multiples menciones"),
})

public abstract class AlgoritmoConsenso {

    public List<Hecho> ejecutarAlgoritmo(List<Fuente> fuentes,List<Hecho> hechos) {
        return hechos;
    }

    public boolean sonHechosIguales(Hecho hecho1, Hecho hecho2) {
        if (hecho1 == null || hecho2 == null) {
            return false;
        }

        // Comparar por título (ignorando mayúsculas/minúsculas)
        boolean titulosIguales = hecho1.getTitulo() != null &&
                hecho2.getTitulo() != null &&
                hecho1.getTitulo().equalsIgnoreCase(hecho2.getTitulo());

        // Comparar por fecha de suceso si ambas existen
        boolean fechasIguales = hecho1.getFechaSuceso() != null &&
                hecho2.getFechaSuceso() != null &&
                hecho1.getFechaSuceso().equals(hecho2.getFechaSuceso());

        // Comparar por ubicación si ambas existen
        boolean ubicacionesIguales = hecho1.getUbicacion() != null &&
                hecho2.getUbicacion() != null &&
                hecho1.getUbicacion().equals(hecho2.getUbicacion());

        return titulosIguales && fechasIguales && ubicacionesIguales;
    }

    public boolean sonHechosSimilares(Hecho hecho1, Hecho hecho2) {
        if (hecho1 == null || hecho2 == null) {
            return false;
        }

        // Comparar por título (ignorando mayúsculas/minúsculas)
        boolean titulosIguales = hecho1.getTitulo() != null &&
                hecho2.getTitulo() != null &&
                !hecho1.getTitulo().equalsIgnoreCase(hecho2.getTitulo());

        // Comparar por fecha de suceso si ambas existen
        boolean fechasDistintas = hecho1.getFechaSuceso() != null &&
                hecho2.getFechaSuceso() != null &&
                !hecho1.getFechaSuceso().equals(hecho2.getFechaSuceso());

        // Comparar por ubicación si ambas existen
        boolean ubicacionesDistintas = hecho1.getUbicacion() != null &&
                hecho2.getUbicacion() != null &&
                !hecho1.getUbicacion().equals(hecho2.getUbicacion());

        return titulosIguales && fechasDistintas && ubicacionesDistintas;
    }
}




