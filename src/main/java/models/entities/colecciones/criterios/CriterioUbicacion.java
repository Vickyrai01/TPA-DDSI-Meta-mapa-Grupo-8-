package models.entities.colecciones.criterios;

import models.entities.hecho.Coordenadas;

public class CriterioUbicacion implements Criterio{
    private Coordenadas coordenadas;

    public CriterioUbicacion(Coordenadas coordenadas){
        this.coordenadas = coordenadas;
    }

    //TENER EN CUENTA QUE TIENE QUE SER EL MISMO OBJETO EN ESPECIFICO!!
    @Override
    public boolean cumpleCriterio(models.entities.hecho.Hecho hecho) {
        return hecho.getUbicacion().equals(coordenadas);
    }
    //TO DO gigante
}
