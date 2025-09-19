package core.models.entities.colecciones.criterios;

import core.models.entities.colecciones.Coleccion;
import core.models.entities.hecho.Hecho;

import java.util.List;
import java.util.stream.Collectors;

public class FiltradorColecciones {

    private static volatile FiltradorColecciones instance;

    private FiltradorColecciones() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() para obtener el Singleton");
        }
    }

    public static FiltradorColecciones getInstance() {
        if (instance == null) {
            synchronized (FiltradorColecciones.class) {
                if (instance == null) {
                    instance = new FiltradorColecciones();
                }
            }
        }
        return instance;
    }

    public List<Hecho> filtrarHechos(List<Hecho> hechos, List<Criterio> criterios) {
        if(criterios == null || criterios.isEmpty())
        {return hechos;}
        else {
            return hechos.stream()
                    .filter(hecho -> criterios.stream().allMatch(criterio -> criterio.cumpleCriterio(hecho)))
                    .collect(Collectors.toList());
        }
    }

    public List<Hecho> filtrarColeccion(Coleccion coleccion, List<Criterio> criterios)
    {
        if(criterios == null || criterios.isEmpty())
        {return coleccion.getHechos();}
        else {
        return filtrarHechos(coleccion.getHechos(), criterios);}
    }

}
