package models.entities.colecciones.criterios;

import models.entities.hecho.Coordenadas;
import models.entities.hecho.Hecho;

public class CriterioUbicacion implements Criterio{
    private Coordenadas coordenadas;

    public CriterioUbicacion(Coordenadas coordenadas){
        this.coordenadas = coordenadas;
    }

    //TENER EN CUENTA QUE TIENE QUE SER EL MISMO OBJETO EN ESPECIFICO!!
    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        return hecho.getUbicacion().getLatitud().equals(coordenadas.getLatitud())  && hecho.getUbicacion().getLongitud().equals(coordenadas.getLongitud())  ;
    }
    //TO DO gigante
}
