package models.entities.hecho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class Coordenadas {
    private Double latitud;
    public Double getLatitud() {
        return latitud;
    }
    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    private Double longitud;
    public Double getLongitud() {
        return longitud;
    }
    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Coordenadas(Double latitud, Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }
    public Coordenadas() {}

    public void cambiarUbicacion(Double latitud, Double longitud){
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String toString(){
        return "(Latitud: " + latitud + "° Longitud: " + longitud + "°)";
    }
}
