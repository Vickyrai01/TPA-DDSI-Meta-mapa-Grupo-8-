package models.services;

import models.entities.fuentes.Fuente;
import models.entities.fuentes.TipoFuente;
import models.entities.hecho.Hecho;

import java.util.ArrayList;
import java.util.List;
import models.entities.hecho.Hecho;
import models.entities.normalizador.HechoAIntegrarDTO;
import models.repository.FuentesRepository;

public interface CargadorFuente {
        List<Fuente> obtenerFuentes();
        List<HechoAIntegrarDTO> extraerHechosAIntegrar();
}