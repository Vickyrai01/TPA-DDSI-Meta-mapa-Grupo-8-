package cargadorDINAMICO.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

public interface StrategyTipoConexion {

    public List<HechoAIntegrarDTO> agregarHecho(String FuenteBase, HechoAIntegrarDTO hecho);

    public List<HechoAIntegrarDTO> extraerHechosRecientes(String fuente, String codigoFuente);
}
