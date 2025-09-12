package models.entities.colecciones;
import models.agregador.normalizador.ComparadorHechos;
import models.entities.fuentes.Fuente;
import models.entities.hecho.Hecho;
import models.repository.HechosRepository;
import models.repository.FuentesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StrategyAbsoluta extends AlgoritmoConsenso {

    @Override
    public List<Hecho> ejecutarAlgoritmo() {

        List<Fuente> fuentes = FuentesRepository.getInstance().obtenerTodas();
        List<Hecho> hechos = HechosRepository.getInstance().obtenerTodas();

        List<Hecho> hechosVisibles = new ArrayList<>();
        List<String> idFuente = new ArrayList<>();

        boolean estaEnFuente = false;
        boolean valido = true;

        for (Fuente fuente : fuentes) {
            String id = fuente.getId().toString();
            if (!idFuente.contains(id)) {
                idFuente.add(id);
            }
        }

        for(Hecho hecho: hechos){
            List<Hecho> hechosIguales = obtenerHechosIguales(hecho, hechos);

            for(String id: idFuente){
                estaEnFuente = hechosIguales.stream()
                        .anyMatch(hechoVerifica -> hechoVerifica.getIdFuente().toString() == id);

                if (!estaEnFuente) {
                    valido = false;
                    break;
                }
            }

            if (valido) {
                hechosVisibles.add(hecho); // solo agrego si pasó todos los chequeos
            }
        }
    return hechosVisibles;
    }
}