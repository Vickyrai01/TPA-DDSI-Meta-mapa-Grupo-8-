package models.entities.hecho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


public class Coordenadas {
    @Setter
    @Getter
    private Double latitud;
    @Setter
    @Getter
    private Double longitud;

    public Coordenadas(Double latitud, Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }
}
