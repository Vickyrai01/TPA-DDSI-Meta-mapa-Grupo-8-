package cargadorProxy.model;

import core.api.DTO.HechoAIntegrarDTO;
import core.models.entities.fuentes.StrategyTipoConexion;

import java.util.List;

public class StrategyBibliotecaCliente implements StrategyTipoConexion {

    @Override
    public List<HechoAIntegrarDTO> extraerHecho(String fuente, String codigoFuente){return null;}; //toma los hechos!!


}
