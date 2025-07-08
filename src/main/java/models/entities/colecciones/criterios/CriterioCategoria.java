package models.entities.colecciones.criterios;

import models.entities.hecho.Categoria;
import models.entities.hecho.Hecho;

public class CriterioCategoria implements Criterio{
    private Categoria categoria;

    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        return hecho.getCategoria().equals(categoria);
    }


}
