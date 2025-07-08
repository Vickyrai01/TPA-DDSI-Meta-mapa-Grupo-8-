package models.entities.colecciones;
import models.entities.fuentes.Fuente;
import models.entities.hecho.Hecho;
import java.util.ArrayList;
import java.util.List;

public class StrategyAbsoluta extends AlgoritmoConsenso {

    @Override
    public List<Hecho> ejecutarAlgoritmo(List<Fuente> fuentes, List<Hecho> hechos) {
        List<Hecho> hechosVisibles = new ArrayList<>();

        for (Hecho hecho : hechos) {
            boolean estaEnTodasLasFuentes = true;

            for (Fuente fuente : fuentes) {
                List<Hecho> hechosDeLaFuente = fuente.extraerHechos(null);
                boolean hechoEncontradoEnFuente = false;

                for (Hecho hechoFuente : hechosDeLaFuente) {
                    if (sonHechosIguales(hecho, hechoFuente)) {
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


