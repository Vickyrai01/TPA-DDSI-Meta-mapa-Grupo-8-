package application.dto;

public record ColeccionDTO(
        Integer id,
        String titulo,
        String descripcionColeccion,
        Object criterioDePertenencia,
        Object hechos,
        Object hechosVisibles,
        String identificadorHandle,
        Object fuentes,
        Integer cantidadHechos,
        String algoritmoConsenso,
        String modoDeNavegacion
) {}
