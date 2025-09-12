package cargadorProxy.model;

import cargadorProxy.repository.RepositoryFuentes;

import java.util.ArrayList;
import java.util.List;

public class CargadorProxy {
    private static CargadorProxy instance;
    private RepositoryFuentes repositoryFuentes = RepositoryFuentes.getInstance() ;
    private List<HechoAIntegrarDTO> hechos = new ArrayList<>();

    public static CargadorProxy getInstance() {
        if (instance == null) {
            synchronized (CargadorProxy.class) {
                if (instance == null) {
                    instance = new CargadorProxy();
                }
            }
        }
        return instance;
    }

    public List<HechoAIntegrarDTO> extraerHechosAIntegrar(){
        List<Fuente> fuentes = repositoryFuentes.getAll();
        for (Fuente f : fuentes) {
            hechos.addAll(f.extraerHechos());
        }
        hechos.forEach(h -> h.setTipoFuente("PROXY"));
        return hechos;
    }

    public Void agregarFuente(){
        return null;
    }
}
