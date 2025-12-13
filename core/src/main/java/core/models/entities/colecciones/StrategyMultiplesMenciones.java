package core.models.entities.colecciones;

import core.models.entities.fuentes.Fuente;
import core.models.entities.hecho.Hecho;
import core.models.repository.HechosRepository;
import core.models.repository.FuentesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StrategyMultiplesMenciones extends AlgoritmoConsenso {

    @Override
    public List<Hecho> ejecutarAlgoritmo(List<Hecho> hechos, List<Fuente> fuentes) {
        List<Hecho> hechosVisibles = new ArrayList<>();

        // Recorremos los hechos de la colección
        for (Hecho hecho : hechos) {

            // PASO 1: Agrupar hechos idénticos (Por Hash o por la lógica de esElMismoHecho)
            // Si quieres forzar que sea POR HASH, cambia 'esElMismoHecho' por comparación de Strings
            List<Hecho> grupo = hechos.stream()
                    .filter(h -> h.getHash().equals(hecho.getHash())) // Comparación estricta de Hash
                    .toList();

            // PASO 2: Contar fuentes distintas
            Set<Integer> idsFuentesEncontradas = grupo.stream()
                    .map(Hecho::getIdFuente)
                    .collect(Collectors.toSet());

            // PASO 3: Validar regla (Al menos 2 fuentes distintas)
            if (idsFuentesEncontradas.size() >= 2) {
                // Evitamos agregar duplicados visuales a la lista final
                boolean yaAgregado = hechosVisibles.stream()
                        .anyMatch(hv -> hv.getHash().equals(hecho.getHash()));

                if (!yaAgregado) {
                    hechosVisibles.add(hecho);
                }
            }
        }
        return hechosVisibles;
    }

    @Override
    public String devolverTipoDeConsenso(){
        return "MULTIPLES_MENCIONES";
    }
}