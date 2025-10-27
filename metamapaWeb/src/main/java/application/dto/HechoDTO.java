package application.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record HechoDTO(
    String hash,
    String nombre,
    String descripcion,
    String contribuyente,
    LocalDate fechaSuceso,
    LocalTime horaSuceso,
    List<String> multimedia,
    List<String> etiquetas,
    String latitud,
    String longitud
) {}
