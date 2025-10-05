package core.models.entities.colecciones;

import core.models.entities.fuentes.Fuente;
import core.models.entities.hecho.Hecho;

import java.util.List;

public class SchedulerActualizarVisibles {
    public Coleccion coleccion;

    public boolean hayBajaCargaEnSistema(){ //suposición
        int hora = java.time.LocalTime.now().getHour();
        return hora >= 1 && hora <= 6;
    }

    public void actualizarColeccion(){ //ver cuando se usa
        if(hayBajaCargaEnSistema()){
            coleccion.actualizarColeccionVisible();
        }
    }
}
