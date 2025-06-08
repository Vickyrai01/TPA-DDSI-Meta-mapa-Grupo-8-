package models.entities.hecho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Coordenadas {
    @Setter
    @Getter
    private Double latitud;
    @Setter
    @Getter
    private Double longitud;

}
