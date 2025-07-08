package models.entities.colecciones;
import models.entities.fuentes.Fuente;
import models.entities.hecho.Hecho;
import java.util.ArrayList;
import java.util.List;
import models.entities.colecciones.AlgoritmoConsenso;

public class StrategyAbsoluta extends AlgoritmoConsenso {

    @Override
    public List<Hecho> ejecutarAlgoritmo(List<Fuente> fuentes, List<Hecho> hechos) {
        List<Hecho> hechosVisibles = new ArrayList<>();

        for (Hecho hecho : hechos) {
            boolean estaEnTodasLasFuentes = true;

            for (Fuente fuente : fuentes) {
                String stringFuente = "fuente";//ver esto
                List<Hecho> hechosDeLaFuente = fuente.extraerHechos(null, stringFuente );//criterio de pertenencia y string de fuente????
                boolean hechoEncontradoEnFuente = false;

                for (Hecho hechoFuente : hechosDeLaFuente) {
                    if (sonHechosSimilares(hecho, hechoFuente)) {
                        hechoEncontradoEnFuente = true;
                        break;
                    }
                }

                if (!hechoEncontradoEnFuente) {
                    estaEnTodasLasFuentes = false;
                    break;
                }
            }

            if (estaEnTodasLasFuentes) {
                hechosVisibles.add(hecho);
            }
        }

        return hechosVisibles;
    }

}


