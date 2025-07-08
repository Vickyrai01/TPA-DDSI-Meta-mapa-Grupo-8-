package models.entities.colecciones.criterios;

import models.entities.hecho.Hecho;

public class CriterioNombre implements Criterio{
    private String palabraClave;

    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        return hecho.getTitulo().toLowerCase().contains(palabraClave.toLowerCase());
    }

}
