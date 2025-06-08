package models.repository;

import models.entities.hecho.Hecho;

import java.util.List;

public class HechosRepository {

    private List<Hecho> hechos;

    public void addHecho(Hecho h){
        hechos.add(h);
    }

    public List<Hecho> getHechos(){
        return hechos;
    }

    public void deleteHecho(Hecho h){
        hechos.remove(h);
    }
}
