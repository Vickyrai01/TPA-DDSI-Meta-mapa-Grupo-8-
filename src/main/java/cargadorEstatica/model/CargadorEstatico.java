package cargadorEstatica.model;
import cargadorEstatica.model.RepositoryFuentes;
import cargadorProxy.model.Fuente;

import java.util.ArrayList;
import java.util.List;

public class CargadorEstatico {

    private static CargadorEstatico instance;
    private RepositoryFuentes repositoryFuentes = RepositoryFuentes.getInstance() ;
    private List<HechoAIntegrarDTO> hechos = new ArrayList<>();

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

    public List<HechoAIntegrarDTO> extraerHechosAIntegrar(){
        List<cargadorEstatica.model.Fuente> fuentes = repositoryFuentes.getAll();
        fuentes.forEach(f -> hechos.addAll(f.extraerHechos()));
        hechos.forEach(h -> h.setTipoFuente("ESTATICA"));
        return hechos;
    }

}
