package cargadorProxy.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Fuente {

    private Integer id;
    private String nombre;
    private String link;

    @JsonIgnore
    private StrategyTipoConexion strategyManeraDeObtenerHechos;
    private String codigoFuente;

    public Fuente(Integer id, String nombre, String link, StrategyTipoConexion strategyManeraDeObtenerHechos, String codigoFuente) {
        this.id = id;
        this.nombre = nombre;
        this.link = link;
        this.strategyManeraDeObtenerHechos = strategyManeraDeObtenerHechos;
        this.codigoFuente = codigoFuente;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public StrategyTipoConexion getStrategyManeraDeObtenerHechos() {
        return strategyManeraDeObtenerHechos;
    }

    public void setStrategyManeraDeObtenerHechos(StrategyTipoConexion strategyManeraDeObtenerHechos) {
        this.strategyManeraDeObtenerHechos = strategyManeraDeObtenerHechos;
    }

    public String getCodigoFuente() {
        return codigoFuente;
    }

    public void setCodigoFuente(String codigoFuente) {
        this.codigoFuente = codigoFuente;
    }

    public List<HechoAIntegrarDTO> extraerHechos(){
        return strategyManeraDeObtenerHechos.extraerHechosRecientes(link, codigoFuente);
    }
}
