package models.entities.hecho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class Coordenadas {
    public Double getLongitud() {
        return longitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    private Double latitud;

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    private Double longitud;

    public Coordenadas(Double latitud, Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public void cambiarUbicacion(Double latitud, Double longitud){
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String toString(){
        return "(Latitud: " + latitud + "° Longitud: " + longitud + "°)";
    }
}
