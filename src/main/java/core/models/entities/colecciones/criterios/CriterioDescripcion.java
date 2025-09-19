package core.models.entities.colecciones.criterios;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import core.models.entities.hecho.Hecho;

public class CriterioDescripcion implements Criterio{
    private String palabraClave;

    public CriterioDescripcion(String palabraClave) {
        this.palabraClave = palabraClave;
    }
    @JsonSerialize(as = String.class)
    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        return hecho.getDescripcion().toLowerCase().contains(palabraClave.toLowerCase());
    }
}
