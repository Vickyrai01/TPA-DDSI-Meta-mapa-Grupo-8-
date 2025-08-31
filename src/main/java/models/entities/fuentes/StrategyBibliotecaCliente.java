package models.entities.fuentes;

import models.entities.colecciones.criterios.Criterio;
import models.entities.hecho.Hecho;
import models.entities.normalizador.HechoAIntegrarDTO;

import java.util.List;

public class StrategyBibliotecaCliente implements StrategyTipoConexion {

    @Override
    public List<HechoAIntegrarDTO> extraerHecho(String fuente, String codigoFuente){return null;}; //toma los hechos!!


}
