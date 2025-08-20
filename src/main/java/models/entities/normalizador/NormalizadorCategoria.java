package models.entities.normalizador;

import api.dto.HechoDTO;
import models.entities.hecho.Categoria;
import models.entities.hecho.Hecho;

public class NormalizadorCategoria {

    private static NormalizadorCategoria instance;

    public static NormalizadorCategoria getInstance(){
        if (instance == null) instance = new NormalizadorCategoria();

        return instance;
    };

    public Categoria obtenerCategoria(HechoAIntegrarDTO hecho){
        //Necesitamos un repositorio de Categorias? para saber donde buscar
        return new Categoria(hecho.getCategoria());
    }

    /*
    public Hecho normalizar(HechoAIntegrarDTO hecho){ }*/
}
