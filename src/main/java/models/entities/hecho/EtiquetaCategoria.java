package models.entities.hecho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class EtiquetaCategoria extends Etiqueta {
    @Setter
    @Getter
    private String nombre;
}
