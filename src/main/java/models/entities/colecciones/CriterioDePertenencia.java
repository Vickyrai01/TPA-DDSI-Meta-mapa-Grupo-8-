package models.entities.colecciones;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import models.entities.hecho.Etiqueta;

import java.util.List;
@AllArgsConstructor
public class CriterioDePertenencia {
    @Setter
    @Getter
    private String titulo;
    @Setter
    @Getter
    private String descripcion;
    @Setter
    @Getter
    private List<Etiqueta> etiquetas;
}
