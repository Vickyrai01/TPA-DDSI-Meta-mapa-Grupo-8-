package core.api.DTO;

import core.models.entities.colecciones.criterios.Criterio;

import java.util.List;

public class ActualizoColeccionDTO {
    public String titulo;
    public String descripcionColeccion;
    public List<Criterio> criterioDePertenencia;
    public List<Integer> hechos;
    public List<Integer> fuentes;

    public ActualizoColeccionDTO() {}
}
