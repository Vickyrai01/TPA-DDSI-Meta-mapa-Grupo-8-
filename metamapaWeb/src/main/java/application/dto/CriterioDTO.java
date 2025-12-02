package application.dto;

import java.time.LocalDate;

public record CriterioDTO(
        String type,        // "nombre", "descripcion", "categoria", "ubicacion", "fechaSuceso", "fechaCarga"
        String palabraClave,
        String categoria,
        Double latitud,
        Double longitud,
        LocalDate desde,
        LocalDate hasta
) {}
