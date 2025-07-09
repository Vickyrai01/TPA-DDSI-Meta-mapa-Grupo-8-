package api.dto;

import models.entities.colecciones.criterios.Criterio;
import models.entities.fuentes.Fuente;

import java.util.List;

public class ActualizoColeccionDTO {
    public String titulo;
    public String descripcionColeccion;
    public List<Fuente> fuente;
    public List<Criterio> criterioDePertenencia;
    public List<Integer> hechos;
    public String identificadorHandle;

    public ActualizoColeccionDTO() {}
}
