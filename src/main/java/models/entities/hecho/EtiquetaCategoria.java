package models.entities.hecho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


public class EtiquetaCategoria extends Etiqueta {
    @Setter
    @Getter
    private String nombre;

    public EtiquetaCategoria(String nombre) {
        this.nombre = nombre;
    }
}
