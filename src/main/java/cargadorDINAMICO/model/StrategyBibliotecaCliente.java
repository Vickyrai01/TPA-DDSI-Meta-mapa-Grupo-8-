package cargadorDINAMICO.model;

import models.entities.colecciones.criterios.Criterio;
import models.entities.fuentes.StrategyTipoConexion;
import models.entities.hecho.Hecho;

import java.util.List;

public class StrategyBibliotecaCliente implements StrategyTipoConexion {

    @Override
    public List<Hecho> extraerHecho(List<Criterio> criterio, String fuente, String codigoFuente){return null;}; //toma los hechos!!

    @Override
    public List<Hecho> agregarHecho(String FuenteBase,  Hecho hecho) {return null;} //es un POST a la fuente

    @Override
    public List<Hecho> extraerHechosRecientes(String fuente, String codigoFuente){
        return null;
    }
}
