package models.entities.colecciones;

import models.entities.fuentes.Fuente;
import models.entities.hecho.Hecho;

import java.util.ArrayList;
import java.util.List;

public class StrategyMayoriaSimple extends AlgoritmoConsenso {

    @Override
    public List<Hecho> ejecutarAlgoritmo(List<Fuente> fuentes, List<Hecho> hechos) {
        List<Hecho> hechosVisibles = new ArrayList<>();

        int minimoFuentesRequeridas = (int) (fuentes.size() / 2);

        for (Hecho hecho : hechos) {
            int fuentesQueContienen = 0;

            for (Fuente fuente : fuentes) {
                String stringFuente = "url";//ver esto
                List<Hecho> hechosDeLaFuente = fuente.extraerHechos(null, stringFuente );//criterio de pertenencia y string de la fuente???????

                boolean hechoEncontradoEnFuente = false;
                for (Hecho hechoFuente : hechosDeLaFuente) {
                    if (sonHechosSimilares(hecho, hechoFuente)) {
                        fuentesQueContienen++;
                        break;
                    }
                }
            }

            if (fuentesQueContienen >= minimoFuentesRequeridas) {
                hechosVisibles.add(hecho);
            }
        }

        return hechosVisibles;
    }
}
