package models.entities.colecciones.criterios;

import models.entities.hecho.Hecho;

public class CriterioDescripcion implements Criterio{
    private String palabraClave;

    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        return hecho.getDescripcion().toLowerCase().contains(palabraClave.toLowerCase());
    }
}
