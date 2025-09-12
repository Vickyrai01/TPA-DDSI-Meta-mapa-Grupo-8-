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
        verificarSimilares = obtenerHechosSimilares(hecho, hechos);
        boolean haySimilar = false;

        for (Hecho hechoSimilar : verificarSimilares) {
            haySimilar = esHechoSimilar(hecho, hechoSimilar);

            if(haySimilar){
                break;
            }
        }
        return haySimilar;
    }

    public boolean alMenosDos(Hecho hecho) {
        boolean estaEnFuente = false;
        int valido = 0;
        List<Hecho> hechosIguales = obtenerHechosIguales(hecho, hechos);

        for (String id : idFuente) {
            estaEnFuente = hechosIguales.stream()
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
}