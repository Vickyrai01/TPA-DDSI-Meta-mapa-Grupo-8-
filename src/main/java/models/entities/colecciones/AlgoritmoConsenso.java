package models.entities.colecciones;
import models.entities.hecho.Hecho;
import models.entities.fuentes.Fuente;

import java.util.List;
public class AlgoritmoConsenso {

    public List<Hecho> ejecutarAlgoritmo(List<Fuente> fuentes,List<Hecho> hechos) {

        return hechos;
    }

    public boolean sonHechosSimilares(Hecho hecho1, Hecho hecho2) {
        if (hecho1 == null || hecho2 == null) {
            return false;
        }

        // Comparar por título (ignorando mayúsculas/minúsculas)
        boolean titulosSimilares = hecho1.getTitulo() != null &&
                hecho2.getTitulo() != null &&
                hecho1.getTitulo().equalsIgnoreCase(hecho2.getTitulo());

        // Comparar por fecha de suceso si ambas existen
        boolean fechasSimilares = hecho1.getFechaSuceso() != null &&
                hecho2.getFechaSuceso() != null &&
                hecho1.getFechaSuceso().equals(hecho2.getFechaSuceso());

        // Comparar por ubicación si ambas existen
        boolean ubicacionesSimilares = hecho1.getUbicacion() != null &&
                hecho2.getUbicacion() != null &&
                hecho1.getUbicacion().equals(hecho2.getUbicacion());

        // Se considera similar si el título coincide y al menos uno de los otros criterios también
        return titulosSimilares && (fechasSimilares || ubicacionesSimilares);
    }
}




