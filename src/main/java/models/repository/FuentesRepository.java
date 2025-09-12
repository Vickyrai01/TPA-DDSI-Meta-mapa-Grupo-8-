package models.repository;

import models.entities.colecciones.Coleccion;
import models.entities.fuentes.Fuente;
import models.entities.fuentes.TipoFuente;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private static final List<Fuente> fuentes = new ArrayList<>();

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

    public static List<Fuente> filtrarFuente(String tipoFuente) {
        TipoFuente tipoFuenteClase = TipoFuente.valueOf(tipoFuente.trim().toUpperCase());
        return fuentes.stream()
                .filter(f -> tipoFuenteClase.equals(f.getTipoFuente()))
                .collect(Collectors.toList());
    }
}
