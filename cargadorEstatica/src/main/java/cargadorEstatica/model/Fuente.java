package cargadorEstatica.model;
import core.models.entities.fuentes.TipoConexion;

import java.util.List;

public class Fuente {

    private Integer id;
    private String codigoFuente;
    private String nombre;
    private String link;
    private TipoConexion strategyManeraDeObtenerHechos;

    public Fuente(Integer id, String nombre, String link, TipoConexion strategyManeraDeObtenerHechos, String codigoFuente) {
        this.id = id;
        this.nombre = nombre;
        this.link = link;
        this.strategyManeraDeObtenerHechos = strategyManeraDeObtenerHechos;
        this.codigoFuente = codigoFuente;
    }



    public List<HechoAIntegrarDTO> extraerHechos(){
        if (strategyManeraDeObtenerHechos == TipoConexion.APIREST) {
            //return strategyManeraDeObtenerHechos.extraerHechosRecientes(link, codigoFuente);
        }
        //else();
        return List.of();
    }
}