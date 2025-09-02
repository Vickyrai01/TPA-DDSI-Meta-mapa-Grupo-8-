package models.entities.colecciones;

import models.entities.fuentes.Fuente;
import models.entities.hecho.Hecho;
import models.repository.HechosRepository;
import models.repository.FuentesRepository;

import java.util.ArrayList;
import java.util.List;

public class StrategyMultiplesMenciones extends AlgoritmoConsenso {

    List<Fuente> fuentes = FuentesRepository.getInstance().obtenerTodas();
    List<Hecho> hechos = HechosRepository.getInstance().obtenerTodas();

    List<Hecho> hechosVisibles = new ArrayList<>();
    List<String> idFuente = new ArrayList<>();
    List<Hecho> verificarSimilares = new ArrayList<>();

    @Override
    public List<Hecho> ejecutarAlgoritmo() {
        for(Fuente fuente : fuentes) {
            String id = fuente.getId().toString();
            if (!idFuente.contains(id)) {
                idFuente.add(id);
            }
        }

        for (Hecho hecho : hechos) {
            if (alMenosDos(hecho) && ningunOtro(hecho)) {
                hechosVisibles.add(hecho);
            }
        }
        return hechosVisibles;
    }

    public boolean ningunOtro(Hecho hecho) {
        verificarSimilares = obtenerHechosDiferentesAHash(hechos, hecho.getHash());
        boolean haySimilar = false;

        for (Hecho hechoSimilar : verificarSimilares) {
            haySimilar = sonHechosSimilares(hecho, hechoSimilar);

            if(haySimilar){
                break;
            }
        }

        return haySimilar;
    }

    public boolean alMenosDos(Hecho hecho) {
        boolean estaEnFuente = false;
        int valido = 0;
        String hash = hecho.getHash();
        List<Hecho> hechosMismoHash = obtenerHechosPorHash(hechos, hash);

        for (String id : idFuente) {
            estaEnFuente = hechosMismoHash.stream()
                    .anyMatch(hechoVerifica -> hechoVerifica.getIdFuente().toString() == id);
            if (estaEnFuente) {
                valido++;
            }
        }

        if(valido >= 2) {
            return true;
        }
        return false;
    }

    public List<Hecho> obtenerHechosDiferentesAHash(List<Hecho> hechos, String hash) {
        List<Hecho> hechosConDiferenteHash = new ArrayList<>();

        for (Hecho hecho : hechos) {
            if (!hecho.getHash().equals(hash)) {
                hechosConDiferenteHash.add(hecho);
            }
        }

        return hechosConDiferenteHash;
    }

public boolean sonHechosSimilares(Hecho hecho1, Hecho hecho2) {
        if (hecho1 == null || hecho2 == null) {
            return false;
        }

        // Comparar por título (ignorando mayúsculas/minúsculas)
        boolean titulosIguales = hecho1.getTitulo() != null &&
                hecho2.getTitulo() != null &&
                !hecho1.getTitulo().equalsIgnoreCase(hecho2.getTitulo());

        // Comparar por fecha de suceso si ambas existen
        boolean fechasDistintas = hecho1.getFechaSuceso() != null &&
                hecho2.getFechaSuceso() != null &&
                !hecho1.getFechaSuceso().equals(hecho2.getFechaSuceso());

        // Comparar por ubicación si ambas existen
        boolean ubicacionesDistintas = hecho1.getUbicacion() != null &&
                hecho2.getUbicacion() != null &&
                !hecho1.getUbicacion().equals(hecho2.getUbicacion());

        return titulosIguales && fechasDistintas && ubicacionesDistintas;
    }
}