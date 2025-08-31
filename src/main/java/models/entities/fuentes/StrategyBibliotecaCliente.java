package models.entities.fuentes;

import models.entities.hecho.HechoAIntegrarDTO;

import java.util.List;

public class StrategyBibliotecaCliente implements StrategyTipoConexion {

    @Override
    public List<HechoAIntegrarDTO> extraerHecho(String fuente, String codigoFuente){return null;}; //toma los hechos!!


}
