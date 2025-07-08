package models.entities.colecciones;

import lombok.AllArgsConstructor;
import models.entities.fuentes.Fuente;
import models.entities.fuentes.StrategyTipoConexion;
import models.entities.hecho.Hecho;

import java.util.List;


public interface ModoNavegacion{

    public List<Hecho> modoDeNavegacion(List<Hecho> hechos, AlgoritmoConsenso algortimoConsenso);

}