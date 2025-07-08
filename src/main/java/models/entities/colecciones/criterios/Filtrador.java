package models.entities.colecciones.criterios;

import models.entities.hecho.Hecho;
import models.repository.ColeccionesRepository;

import java.util.List;
import java.util.stream.Collectors;

public class Filtrador {

    private static volatile Filtrador instance;

    private Filtrador() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() para obtener el Singleton");
        }
    }

    public static Filtrador getInstance() {
        if (instance == null) {
            synchronized (Filtrador.class) {
                if (instance == null) {
                    instance = new Filtrador();
                }
            }
        }
        return instance;
    }

    public List<Hecho> filtrarHechos(List<Hecho> hechos, List<Criterio> criterios) {
        return hechos.stream()
                .filter(hecho -> criterios.stream().allMatch(criterio -> criterio.cumpleCriterio(hecho)))
                .collect(Collectors.toList());
    }

    public Boolean cumpleCriterios(Hecho hecho, List<Criterio> criterios) {
        return criterios.stream().allMatch(criterio -> criterio.cumpleCriterio(hecho));
    }
}
