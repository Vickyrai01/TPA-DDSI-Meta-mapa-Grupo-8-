package core.models.agregador.cargadores;

import core.models.entities.fuentes.Fuente;

import java.util.List;

import core.api.DTO.HechoAIntegrarDTO;

public interface CargadorFuente {
        List<Fuente> obtenerFuentes();
        List<HechoAIntegrarDTO> extraerHechosAIntegrar();
}