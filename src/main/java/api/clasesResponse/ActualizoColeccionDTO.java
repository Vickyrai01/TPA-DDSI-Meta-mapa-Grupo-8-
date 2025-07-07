package api.clasesResponse;

import models.entities.colecciones.CriterioDePertenencia;
import models.entities.fuentes.Fuente;

import java.util.List;

public class ActualizoColeccionDTO {
    public String titulo;
    public String descripcionColeccion;
    public List<Fuente> fuente;
    public CriterioDePertenencia criterioDePertenencia;
    public List<Integer> hechos;
    public String identificadorHandle;

    public ActualizoColeccionDTO() {}
}
