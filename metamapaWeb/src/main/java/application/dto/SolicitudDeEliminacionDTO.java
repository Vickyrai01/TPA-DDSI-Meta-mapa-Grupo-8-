package application.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record SolicitudDeEliminacionDTO(Integer id, String descripcionSolicitud,
                                        String hash, String nombre, String descripcionHecho,
                                        String contribuyente, LocalDate fechaCarga,  LocalDate fechaSuceso,
                                        LocalTime horaSuceso) {
}
