package models.entities.fuentes;

import models.entities.hecho.Hecho;

import java.util.Map;

public interface StrategyTipoConexion {

    public Map<String, Hecho> agregarHecho(String FuenteBase);

}
