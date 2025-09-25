package core.models.entities.colecciones;

import core.models.entities.fuentes.Fuente;
import core.models.entities.hecho.Hecho;
import core.models.repository.HechosRepository;
import core.models.repository.FuentesRepository;

import java.util.ArrayList;
import java.util.List;

public class StrategyMayoriaSimple extends AlgoritmoConsenso {

    @Override
    public List<Hecho> ejecutarAlgoritmo() {
        List<Fuente> fuentes = FuentesRepository.getInstance().obtenerTodas();
        List<Hecho> hechos = HechosRepository.getInstance().obtenerTodas();

        List<Hecho> hechosVisibles = new ArrayList<>();
        List<String> idFuente = new ArrayList<>();

        boolean estaEnFuente = false;

        for (Fuente fuente : fuentes) {
            String id = fuente.getId().toString();
            if (!idFuente.contains(id)) {
                idFuente.add(id);
            }
        }

        for(Hecho hecho: hechos){
            int valido = 0;
            List<Hecho> hechosIguales = obtenerHechosIguales(hecho, hechos);

            for(String id: idFuente){
                estaEnFuente = hechosIguales.stream()
                        .anyMatch(hechoVerifica -> hechoVerifica.getIdFuente().toString() == id);

                if (estaEnFuente) {
                    valido++;
                }
            }

            if (valido>=fuentes.size()/2) {
                hechosVisibles.add(hecho);
            }
        }
        return hechosVisibles;
    }
}