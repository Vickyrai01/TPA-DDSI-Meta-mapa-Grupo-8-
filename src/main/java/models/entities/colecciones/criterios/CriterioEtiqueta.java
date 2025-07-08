package models.entities.colecciones.criterios;

import models.entities.hecho.Etiqueta;
import models.entities.hecho.Hecho;

public class CriterioEtiqueta implements Criterio{
    private Etiqueta etiqueta;

    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        return hecho.getEtiquetas().contains(etiqueta);
    }
}
