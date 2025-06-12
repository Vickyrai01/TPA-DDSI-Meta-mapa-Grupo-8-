package models.repository;

import models.entities.solicitud.SolicitudDeEliminacion;

import java.util.ArrayList;
import java.util.List;

public class SolicitudEliminacionRepository {
    private static volatile SolicitudEliminacionRepository instance;

    private SolicitudEliminacionRepository() {
        if (instance != null) {
            throw new RuntimeException("¡Usa getInstance() para obtener el Singleton!");
        }
    }

    public static SolicitudEliminacionRepository getInstance() {
        if (instance == null) { // Primera verificación (sin bloqueo, mejora el rendimiento)
            synchronized (SolicitudEliminacionRepository.class) { // Bloqueo para evitar race conditions
                if (instance == null) { // Segunda verificación (dentro del bloqueo)
                    instance = new SolicitudEliminacionRepository();
                }
            }
        }
        return instance;
    }

    private static final List<SolicitudDeEliminacion> solicitudes = new ArrayList<>();

    public void add(SolicitudDeEliminacion s){
        solicitudes.add(s);
    }

    public static List<SolicitudDeEliminacion> obtenerTodas(){
        return solicitudes;
    }

    public void deleteSolicitudes(SolicitudDeEliminacion s){
        solicitudes.remove(s);
    }
}
