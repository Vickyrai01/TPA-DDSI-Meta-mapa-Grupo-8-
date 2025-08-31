package models.entities.fuentes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.entities.colecciones.criterios.Criterio;
import models.entities.hecho.Hecho;
import models.entities.normalizador.HechoAIntegrarDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Fuente {

    public Fuente(String nombre, String link, TipoFuente tipoFuente, StrategyTipoConexion strategyTipoConexion) {
        this.nombre = nombre;
        this.link = link;
        this.tipoFuente = tipoFuente;
        this.strategyTipoConexion = strategyTipoConexion;
        this.ultimoProcesado = null;
    }

    public  Fuente(){}

    private Integer id;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    private String nombre;
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private String link;
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    private TipoFuente tipoFuente;
    public TipoFuente getTipoFuente() {
        return tipoFuente;
    }
    public void setTipoFuente(TipoFuente tipoFuente) {
        this.tipoFuente = tipoFuente;
    }

    private String codigoDeFuente;
    public String getCodigoDeFuente() {
        return codigoDeFuente;
    }
    public String setCodigoDeFuente(String codigoDeFuente) {
        return this.codigoDeFuente = codigoDeFuente;
    }

    private LocalDateTime ultimoProcesado;
    public LocalDateTime getUltimoProcesado() {
        return ultimoProcesado;
    }
    public void actualizarUltimoProcesado() {
        this.ultimoProcesado = LocalDateTime.now();
    }

    @JsonIgnore
    private StrategyTipoConexion strategyTipoConexion;
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

