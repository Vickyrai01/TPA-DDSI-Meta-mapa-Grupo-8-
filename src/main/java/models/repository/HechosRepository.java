package models.repository;

import models.entities.hecho.Hecho;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class HechosRepository {

    private static volatile HechosRepository instance;

    private HechosRepository() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() para obtener el Singleton");
        }
    }

    public static HechosRepository getInstance() {
        if (instance == null) { // verifica si hay instancia
            synchronized (HechosRepository.class) { // Bloqueo para evitar condicion de carrera
                if (instance == null) { // verifica nuevamente si existe instancia
                    instance = new HechosRepository();
                }
            }
        }
        return instance;
    }
    private final List<Hecho> hechos = new ArrayList<>();

    public List<Hecho> obtenerTodas(){
       return hechos;
    }

    public void delete(Hecho h){
        hechos.remove(h);
    }

    public  void add(Hecho h){
        hechos.add(h);
    }

}
