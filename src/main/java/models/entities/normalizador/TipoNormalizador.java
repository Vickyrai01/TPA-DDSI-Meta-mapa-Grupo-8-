package models.entities.normalizador;

import models.entities.hecho.Hecho;

public interface TipoNormalizador {
    public Hecho normalizar(HechoAIntegrarDTO hecho);
}
