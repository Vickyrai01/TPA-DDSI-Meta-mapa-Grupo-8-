package models.repository;

import models.entities.hecho.Hecho;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class HechosRepository {

    private static volatile HechosRepository instance;

    private HechosRepository() {
        // Evita la creación de instancias mediante reflection
        if (instance != null) {
            throw new RuntimeException("¡Usa getInstance() para obtener el Singleton!");
        }
    }

    // 3. Método público estático para obtener la instancia (Thread-Safe con doble verificación)
    public static HechosRepository getInstance() {
        if (instance == null) { // Primera verificación (sin bloqueo, mejora el rendimiento)
            synchronized (HechosRepository.class) { // Bloqueo para evitar race conditions
                if (instance == null) { // Segunda verificación (dentro del bloqueo)
                    instance = new HechosRepository();
                }
            }
        }
        return instance;
    }
    private List<Hecho> hechos = new ArrayList<>();

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
