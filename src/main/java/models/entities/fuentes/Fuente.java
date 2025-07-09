package models.entities.fuentes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.entities.colecciones.criterios.Criterio;
import models.entities.hecho.Hecho;

import java.util.List;

public class Fuente {

    public Fuente(String nombre, String link, TipoFuente tipoFuente, StrategyTipoConexion strategyTipoConexion) {
        this.nombre = nombre;
        this.link = link;
        this.tipoFuente = tipoFuente;
        this.strategyTipoConexion = strategyTipoConexion;
    }

    public  Fuente(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    private String nombre;

    private String link;

    private TipoFuente tipoFuente;

    private String codigoDeFuente;

    public String getCodigoDeFuente() {
        return codigoDeFuente;
    }

    public String setCodigoDeFuente(String codigoDeFuente) {
        return this.codigoDeFuente = codigoDeFuente;
    }

    @JsonIgnore
    private StrategyTipoConexion strategyTipoConexion;

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

    public TipoFuente getTipoFuente() {
        return tipoFuente;
    }

    public void setTipoFuente(TipoFuente tipoFuente) {
        this.tipoFuente = tipoFuente;
    }

    public StrategyTipoConexion getStrategyTipoConexion() {
        return strategyTipoConexion;
    }

    public void setStrategyTipoConexion(StrategyTipoConexion strategyTipoConexion) {
        this.strategyTipoConexion = strategyTipoConexion;
    }

    public List<Hecho> extraerHechos(List<Criterio> criterios){
        return strategyTipoConexion.extraerHecho(criterios, link, codigoDeFuente);
    };

    public List<Hecho> extraerHechosRecientes(){
        return strategyTipoConexion.extraerHechosRecientes(link, codigoDeFuente);
    };
}

