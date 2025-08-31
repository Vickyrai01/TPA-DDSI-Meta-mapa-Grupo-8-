package models.entities.colecciones;

import models.entities.fuentes.Fuente;
import models.entities.hecho.Hecho;

import java.util.ArrayList;
import java.util.List;

public class StrategyMultiplesMenciones extends AlgoritmoConsenso {

    @Override
    public List<Hecho> ejecutarAlgoritmo(List<Fuente> fuentes, List<Hecho> hechos) {
        List<Hecho> hechosVisibles = new ArrayList<>();

       /* int minimoFuentesRequeridas = 2;
        boolean similar = false;

        for (Hecho hecho : hechos) {
            int fuentesQueContienen = 0;

            for (Fuente fuente : fuentes) {
                List<Hecho> hechosDeLaFuente = fuente.extraerHechos(null);

                for (Hecho hechoFuente : hechosDeLaFuente) {
                    if (sonHechosIguales(hecho, hechoFuente)) {
                        fuentesQueContienen++;
                        break;
                    }
                    if(sonHechosSimilares(hecho,hechoFuente)){
                        similar = true;
                        break;
                    }
                }
            }

            if (fuentesQueContienen >= minimoFuentesRequeridas && !similar) {
                hechosVisibles.add(hecho);
            }
        }*/

        return hechosVisibles;
    }
}