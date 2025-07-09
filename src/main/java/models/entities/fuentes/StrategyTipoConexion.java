package models.entities.fuentes;

import models.entities.colecciones.criterios.Criterio;
import models.entities.hecho.Hecho;

import java.util.List;
import java.util.Map;

public interface StrategyTipoConexion {

    public List<Hecho> extraerHecho(List<Criterio> criterio, String fuente, String codigoFuente);

    public List<Hecho> agregarHecho(String FuenteBase, Hecho hecho);

    public List<Hecho> extraerHechosRecientes(String fuente, String codigoFuente);
}
