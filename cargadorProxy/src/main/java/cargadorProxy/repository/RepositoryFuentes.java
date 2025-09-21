package cargadorProxy.repository;

import cargadorProxy.model.Fuente;

import java.util.ArrayList;
import java.util.List;

public class RepositoryFuentes {
    private static volatile RepositoryFuentes instance;
    private List<Fuente> fuentesProxy = new ArrayList<>();

    private RepositoryFuentes() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() para obtener el Singleton");
        }
    }

    public static RepositoryFuentes getInstance() {
        if (instance == null) {
            synchronized (RepositoryFuentes.class) {
                if (instance == null) {
                    instance = new RepositoryFuentes();
                }
            }
        }
        return instance;
    }

    public void agregarFuente(Fuente fuente){
        fuentesProxy.add(fuente);
    }


    public List<Fuente> getAll(){return fuentesProxy;}

}
