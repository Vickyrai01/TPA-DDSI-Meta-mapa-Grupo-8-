package core.models.entities.colecciones.criterios;

import core.models.entities.hecho.Etiqueta;
import core.models.entities.hecho.Hecho;

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
