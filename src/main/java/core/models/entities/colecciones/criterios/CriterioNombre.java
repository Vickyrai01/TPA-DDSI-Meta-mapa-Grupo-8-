package core.models.entities.colecciones.criterios;

import core.models.entities.hecho.Hecho;

public class CriterioNombre implements Criterio{
    private String palabraClave;

    public CriterioNombre(String palabraClave){
        this.palabraClave = palabraClave;
    }

    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        return hecho.getTitulo().toLowerCase().contains(palabraClave.toLowerCase());
    }

}
