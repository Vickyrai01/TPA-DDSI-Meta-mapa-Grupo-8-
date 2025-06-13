package models.entities.fuentes;

import models.entities.colecciones.CriterioDePertenencia;
import models.entities.hecho.Hecho;

import java.util.List;
import java.util.Map;

public interface StrategyTipoConexion {

    public List<Hecho> extraerHecho(CriterioDePertenencia criterio, String fuente);

    public List<Hecho> agregarHecho(String FuenteBase, Hecho hecho);

}
