package cargadorEstatica.model;
import cargadorEstatica.repository.RepositoryFuentes;

import java.util.ArrayList;
import java.util.List;

public class CargadorEstatico {

    private static CargadorEstatico instance;
    private RepositoryFuentes repositoryFuentes = RepositoryFuentes.getInstance() ;

    public static CargadorEstatico getInstance() {
        if (instance == null) {
            synchronized (CargadorEstatico.class) {
                if (instance == null) {
                    instance = new CargadorEstatico();
                }
            }
        }
        return instance;
    }

    public List<HechoAIntegrarDTO> extraerHechosAIntegrar() {
        List<Fuente> fuentes = repositoryFuentes.getAll();
        if (fuentes.isEmpty()) return List.of();

        // ✅ lista LOCAL nueva por request (no acumula)
        List<HechoAIntegrarDTO> hechos = new ArrayList<>();
        for (Fuente f : fuentes) {
            List<HechoAIntegrarDTO> lote = f.extraerHechos();
            if (lote != null) hechos.addAll(lote);
        }
        hechos.forEach(h -> h.setTipoFuente("ESTATICA"));
        return hechos;
    }

}
