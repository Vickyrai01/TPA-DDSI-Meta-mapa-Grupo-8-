package models.agregador.cargadores;

import models.entities.fuentes.Fuente;

import java.util.List;

import api.dto.HechoAIntegrarDTO;

public interface CargadorFuente {
        List<Fuente> obtenerFuentes();
        List<HechoAIntegrarDTO> extraerHechosAIntegrar();
}