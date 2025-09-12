package cargadorEstatica.model;
import cargadorEstatica.model.HechoAIntegrarDTO;
import cargadorEstatica.model.StrategyTipoConexion;

import java.util.List;

public class Fuente {

    private Integer id;
    private String nombre;
    private String link;
    private StrategyTipoConexion strategyManeraDeObtenerHechos;
    private String codigoFuente;

    public List<HechoAIntegrarDTO> extraerHechos(){
        return strategyManeraDeObtenerHechos.extraerHechosRecientes(link, codigoFuente);
    }
}
