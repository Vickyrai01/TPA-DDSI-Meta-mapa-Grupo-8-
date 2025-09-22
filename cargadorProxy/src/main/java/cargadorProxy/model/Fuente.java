package cargadorProxy.model;


import java.util.List;

public class Fuente {

    private Integer id;
    private String nombre;
    private String link;
    private StrategyTipoConexion strategyManeraDeObtenerHechos;
    private String codigoFuente;

    public Fuente(Integer id, String nombre, String link, StrategyTipoConexion strategyManeraDeObtenerHechos, String codigoFuente) {
        this.id = id;
        this.nombre = nombre;
        this.link = link;
        this.strategyManeraDeObtenerHechos = strategyManeraDeObtenerHechos;
        this.codigoFuente = codigoFuente;
    }

    public List<HechoAIntegrarDTO> extraerHechos(){
        return strategyManeraDeObtenerHechos.extraerHechosRecientes(link, codigoFuente);
    }
}
