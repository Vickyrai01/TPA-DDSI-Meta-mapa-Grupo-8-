package models.services;

import models.entities.fuentes.Fuente;
import models.entities.fuentes.TipoFuente;
import models.entities.hecho.Hecho;

import java.util.List;

public interface CargadorFuente {
    List<HechoAIntegrarDTO> extraerHechosAIntegrar();
}
