package models.agregador.cargadores;

import models.entities.fuentes.Fuente;

import java.util.List;

import models.entities.hecho.HechoAIntegrarDTO;

public interface CargadorFuente {
        List<Fuente> obtenerFuentes();
        List<HechoAIntegrarDTO> extraerHechosAIntegrar();
}