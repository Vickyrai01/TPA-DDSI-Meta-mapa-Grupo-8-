package models.entities.colecciones.criterios;

import models.entities.hecho.Hecho;

public interface Criterio {
    boolean cumpleCriterio(Hecho hecho);
}
