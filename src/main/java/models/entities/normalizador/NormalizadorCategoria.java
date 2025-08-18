package models.entities.normalizador;

import api.dto.HechoDTO;
import models.entities.hecho.Hecho;

public class NormalizadorCategoria {

    private static NormalizadorCategoria instance;

    public NormalizadorCategoria getInstance(){
        if (instance == null) instance = new NormalizadorCategoria();

        return instance;
    };

    /*
    public Hecho normalizar(HechoAIntegrarDTO hecho){

    }*/
}
