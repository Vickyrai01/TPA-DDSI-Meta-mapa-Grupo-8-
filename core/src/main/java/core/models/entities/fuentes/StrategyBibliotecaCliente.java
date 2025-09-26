package core.models.entities.fuentes;

import core.api.DTO.HechoAIntegrarDTO;

import java.util.List;

public class StrategyBibliotecaCliente implements StrategyTipoConexion {

    @Override
    public List<HechoAIntegrarDTO> extraerHecho(String fuente, String codigoFuente){return null;}; //toma los hechos!!

    @Override
    public String devolverTipoDeConexion() {return "BIBLIOTECA";};

}
