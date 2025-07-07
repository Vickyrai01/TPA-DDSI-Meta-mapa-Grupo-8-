package models.repository;

import models.entities.colecciones.Coleccion;
import models.entities.hecho.Hecho;

import java.util.ArrayList;
import java.util.List;

public class ColeccionesRepository {

    private static volatile ColeccionesRepository instance;

    private ColeccionesRepository() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() para obtener el Singleton");
        }
    }

    public static ColeccionesRepository getInstance() {
        if (instance == null) {
            synchronized (ColeccionesRepository.class) {
                if (instance == null) {
                    instance = new ColeccionesRepository();
                }
            }
        }
        return instance;
    }

    private final List<Coleccion> colecciones = new ArrayList<>();

    public List<Coleccion> obtenerTodas(){
        return colecciones;
    }

    public void delete(Coleccion c){
        colecciones.remove(c);
    }

    public Coleccion getColeccion(int id) {
        return colecciones.stream()
                .filter(h -> h.getId() == id)
                .findFirst()
                .orElse(null);
    }
    public  void add(Coleccion h){
        colecciones.add(h);
    }

}
