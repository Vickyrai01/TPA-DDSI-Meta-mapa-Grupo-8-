package models.entities.normalizador;

public class NormalizadorTitulo {

    private static NormalizadorTitulo instance;

    public NormalizadorTitulo getInstance(){
        if (instance == null) instance = new NormalizadorTitulo();
        return instance;
    }

}
