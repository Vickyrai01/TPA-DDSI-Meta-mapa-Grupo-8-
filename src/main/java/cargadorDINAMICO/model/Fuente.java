package cargadorDINAMICO.model;
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
