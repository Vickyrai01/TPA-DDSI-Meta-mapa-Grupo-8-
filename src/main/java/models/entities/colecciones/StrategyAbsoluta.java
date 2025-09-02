package models.entities.colecciones;
import models.entities.fuentes.Fuente;
import models.entities.hecho.Hecho;
import models.repository.HechosRepository;
import models.repository.FuentesRepository;

import java.util.ArrayList;
import java.util.List;

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
            String hash= hecho.getHash();
            List<Hecho> hechosMismoHash = obtenerHechosPorHash(hechos, hash);

            for(String id: idFuente){
                estaEnFuente = hechosMismoHash.stream()
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

    public List<Hecho> obtenerHechosPorHash(List<Hecho> hechos, String hash) {

        List<Hecho> hechosConMismoHash = new ArrayList<>();

        for (Hecho hecho : hechos) {
            if (hecho.getHash().equals(hash)) {
                hechosConMismoHash.add(hecho);
            }
        }

        return hechosConMismoHash;
    }

}