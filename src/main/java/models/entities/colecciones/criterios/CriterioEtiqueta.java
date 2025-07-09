package models.entities.colecciones.criterios;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import models.entities.hecho.Etiqueta;
import models.entities.hecho.Hecho;

public class CriterioEtiqueta implements Criterio{
    private Etiqueta etiqueta;

    public CriterioEtiqueta(Etiqueta etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        return hecho.getEtiquetas().contains(etiqueta);
    }
}
