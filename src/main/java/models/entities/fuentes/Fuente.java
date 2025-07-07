package models.entities.fuentes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import models.entities.colecciones.CriterioDePertenencia;
import models.entities.hecho.Hecho;

import java.net.URL;
import java.util.List;

public class Fuente {

    public Fuente(int id, String nombre, URL link, TipoFuente tipoFuente, StrategyTipoConexion strategyTipoConexion) {
        this.id = id;
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

    private URL link;

    private TipoFuente tipoFuente;

    @JsonIgnore
    private StrategyTipoConexion strategyTipoConexion;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public URL getLink() {
        return link;
    }

    public void setLink(URL link) {
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

    public List<Hecho> extraerHechos(CriterioDePertenencia criterio){
        return List.of();
    };

    
}

