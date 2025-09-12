package cargadorDINAMICO.model;

import models.services.ServicioDeAgregacion;

import java.util.List;

public class CargadorDinamico {

    private static CargadorDinamico instance;
    private RepositoryFuentes repositoryFuentes;


    public static CargadorDinamico getInstance() {
        if (instance == null) {
            synchronized (CargadorDinamico.class) {
                if (instance == null) {
                    instance = new CargadorDinamico();
                }
            }
        }
        return instance;
    }


    public List<HechoAIntegrarDTO> extraerHechosAIntegrar(){
        return List.of();
    }

    public Void agregarFuente(){
        return null;
    }
}
