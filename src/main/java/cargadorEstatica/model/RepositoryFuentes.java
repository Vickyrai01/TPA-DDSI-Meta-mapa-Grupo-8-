package cargadorEstatica.model;

import cargadorEstatica.model.Fuente;

import java.util.List;

public class RepositoryFuentes {
    private static volatile cargadorEstatica.model.RepositoryFuentes instance;
    private List<cargadorEstatica.model.Fuente> fuentesEstaticas;

    private RepositoryFuentes() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() para obtener el Singleton");
        }
    }

    public static RepositoryFuentes getInstance() {
        if (instance == null) {
            synchronized (cargadorEstatica.model.RepositoryFuentes.class) {
                if (instance == null) {
                    instance = new cargadorEstatica.model.RepositoryFuentes();
                }
            }
        }
        return instance;
    }

    public void agregarFuente(cargadorEstatica.model.Fuente fuente){
        fuentesEstaticas.add(fuente);
    }

    public List<Fuente> getAll(){return fuentesEstaticas;}

}