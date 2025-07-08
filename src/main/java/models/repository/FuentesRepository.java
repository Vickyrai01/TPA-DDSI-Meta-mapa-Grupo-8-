package models.repository;

import models.entities.colecciones.Coleccion;
import models.entities.fuentes.Fuente;

import java.util.ArrayList;
import java.util.List;

public class FuentesRepository {
    private static volatile FuentesRepository instance;

    private FuentesRepository() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() para obtener el Singleton");
        }
    }

    public static FuentesRepository getInstance() {
        if (instance == null) {
            synchronized (FuentesRepository.class) {
                if (instance == null) {
                    instance = new FuentesRepository();
                }
            }
        }
        return instance;
    }

    private final List<Fuente> fuentes = new ArrayList<>();

    public List<Fuente> obtenerTodas(){
        return fuentes;
    }

    public void delete(Coleccion c){
        fuentes.remove(c);
    }

    public Fuente getFuente(int id) {
        return fuentes.stream()
                .filter(h -> h.getId() == id)
                .findFirst()
                .orElse(null);
    }
    public  void add(Fuente h){
        fuentes.add(h);
    }
}
