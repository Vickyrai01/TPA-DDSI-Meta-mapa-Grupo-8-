package models.entities.hecho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class EtiquetaLugar extends Etiqueta {
    @Setter
    @Getter
    private Coordenadas coordenadasLugar;
}
