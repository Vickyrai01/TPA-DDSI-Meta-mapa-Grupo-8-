package models.entities.fuentes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import models.entities.colecciones.CriterioDePertenencia;
import models.entities.hecho.Hecho;

import java.net.URL;
import java.util.List;

@AllArgsConstructor
public class Fuente {

    private String nombre;

    private URL link;

    private TipoFuente tipoFuente;

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

    public List<Hecho> extraerHechos(CriterioDePertenencia criterio,String fuente){
        return List.of();
    };

    
}

