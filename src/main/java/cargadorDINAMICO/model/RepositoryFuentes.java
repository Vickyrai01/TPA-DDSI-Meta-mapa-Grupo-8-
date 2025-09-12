package cargadorDINAMICO.model;

import cargadorDINAMICO.RepositoryFuentesSeeder;
import models.repository.seeders.FuentesRepositorySeeder;

import java.util.List;

public class RepositoryFuentes {
    private static volatile RepositoryFuentes instance;
    private List<Fuente> fuentesDinamicas;

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

    public List<HechoAIntegrarDTO> extraerHechosAIntegrar(){
        return List.of();
    }

    public void agregarFuente(Fuente fuente){
        fuentesDinamicas.add(fuente);
    }

}
