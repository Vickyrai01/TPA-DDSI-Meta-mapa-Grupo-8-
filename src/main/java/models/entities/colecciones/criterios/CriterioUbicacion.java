package models.entities.colecciones.criterios;

import models.entities.hecho.Coordenadas;

public class CriterioUbicacion implements Criterio{
    private Coordenadas coordenadas;

    @Override
    public boolean cumpleCriterio(models.entities.hecho.Hecho hecho) {
        return hecho.getUbicacion().equals(coordenadas);
    }
    //TO DO gigante
}
