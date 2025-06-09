package models.entities.hecho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


public class EtiquetaLugar extends Etiqueta {
    @Setter
    @Getter
    private Coordenadas coordenadasLugar;

    public EtiquetaLugar(Coordenadas coordenadasLugar) {
        this.coordenadasLugar = coordenadasLugar;
    }
}
