package application.dto;

public record ColeccionDTO(
        Integer id,
        String titulo,
        String descripcionColeccion,
        Object criterioDePertenencia,
        Object hechos,
        String identificadorHandle,
        Object fuente,
        Integer cantidadHechos
) {}
