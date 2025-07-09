package models.entities.colecciones;

import models.entities.fuentes.Fuente;
import models.entities.hecho.Hecho;

import java.util.List;

public class SchedulerActualizarVisibles {
    public Coleccion coleccion;

    public boolean hayBajaCargaEnSistema(){ //suposición
        int hora = java.time.LocalTime.now().getHour();
        return hora >= 1 && hora <= 6;
    }

    public void actualizarColeccion(){ //ver cuando se usa
        if(hayBajaCargaEnSistema()){
            List<Fuente> fuentes= coleccion.getFuentes();
            List<Hecho> hechos= coleccion.getHechos();

            coleccion.actualizarColeccionVisible(fuentes, hechos);
        }
    }
}
