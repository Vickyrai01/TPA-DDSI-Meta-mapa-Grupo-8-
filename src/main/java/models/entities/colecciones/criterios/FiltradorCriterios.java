package models.entities.colecciones.criterios;

import models.entities.hecho.Hecho;

import java.util.List;

public class FiltradorCriterios {

    private static volatile FiltradorCriterios instance;

    private FiltradorCriterios() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() para obtener el Singleton");
        }
    }

    public static FiltradorCriterios getInstance() {
        if (instance == null) {
            synchronized (FiltradorCriterios.class) {
                if (instance == null) {
                    instance = new FiltradorCriterios();
                }
            }
        }
        return instance;
    }

    public Boolean cumpleCriterios(Hecho hecho, List<Criterio> criterios) {
        if(criterios == null || criterios.isEmpty())
        {return true;}
        else{
        return criterios.stream().allMatch(criterio -> criterio.cumpleCriterio(hecho));}
    }
}
