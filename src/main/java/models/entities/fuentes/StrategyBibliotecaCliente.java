package models.entities.fuentes;

import models.entities.colecciones.CriterioDePertenencia;
import models.entities.hecho.Hecho;

import java.util.List;
import java.util.Map;

public class StrategyBibliotecaCliente implements StrategyTipoConexion {

    @Override
    public List<Hecho> extraerHecho(CriterioDePertenencia criterio){return null;};

    @Override
    public Map<String, Hecho> agregarHecho(String FuenteBase) {

        return null;
    }
}
