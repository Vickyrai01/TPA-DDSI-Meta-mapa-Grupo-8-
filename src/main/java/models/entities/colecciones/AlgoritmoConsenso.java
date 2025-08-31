package models.entities.colecciones;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import models.entities.hecho.Hecho;
import models.entities.fuentes.Fuente;

import java.util.ArrayList;
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

    //múltiples menciones: si al menos dos fuentes contienen un mismo hecho y ninguna otra fuente contiene otro de igual título pero diferentes atributos, se lo considera consensuado;

    public List<Hecho> ejecutarAlgoritmo() {
        List<Hecho> hechosVisibles = new ArrayList<>();
        return hechosVisibles;
    }
}