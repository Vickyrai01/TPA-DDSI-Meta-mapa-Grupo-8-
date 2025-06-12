package models.repository;

import models.entities.hecho.Hecho;

import java.util.List;

import static java.util.Arrays.asList;

public class HechosRepository {

    private List<Hecho> hechos;

    public List<Hecho> obtenerTodas(){
        Hecho h1 = new Hecho();
        h1.setId(1);
        h1.setTitulo("Hecho 1");
        h1.setDescripcion("Descripcion del hecho 1");

        Hecho h2 = new Hecho();
        h1.setId(2);
        h1.setTitulo("Hecho 2");
        h1.setDescripcion("Descripcion del hecho 2");

        Hecho h3 = new Hecho();
        h1.setId(3);
        h1.setTitulo("Hecho 3");
        h1.setDescripcion("Descripcion del hecho 3");

        return asList(h1,h2,h3);
    }

    public void delete(Hecho h){
        hechos.remove(h);
    }

    public void add(Hecho h){
        hechos.add(h);
    }

}
