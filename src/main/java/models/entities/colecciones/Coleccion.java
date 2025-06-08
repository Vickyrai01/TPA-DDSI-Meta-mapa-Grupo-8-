package models.entities.colecciones;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import models.entities.fuentes.Fuente;
import models.entities.hecho.Hecho;

import java.util.List;
@AllArgsConstructor
public class Coleccion {
    @Setter
    @Getter
    private String titulo;
    @Setter
    @Getter
    private String descripcion;
    @Setter
    @Getter
    private Fuente fuente;
    @Setter
    @Getter
    private CriterioDePertenencia criterioDePertenencia;
    @Setter
    @Getter
    private List<Hecho> hechos;
    @Setter
    @Getter
    private String identificadorHandle;

}
