package models.entities.colecciones.criterios;

import models.entities.hecho.Categoria;
import models.entities.hecho.Hecho;

public class CriterioCategoria implements Criterio{
    private Categoria categoria;

    public CriterioCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    //TENER EN CUENTA QUE TIENE QUE SER EL MISMO OBJETO EN ESPECIFICO!!
    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        return hecho.getCategoria().equals(categoria);
    }


}
