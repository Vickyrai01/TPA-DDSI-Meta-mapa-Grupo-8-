package models.entities.solicitud;

import lombok.Getter;
import lombok.Setter;
import models.entities.hecho.Hecho;

import java.time.LocalDateTime;

public class SolicitudDeEliminacion {
    @Setter
    @Getter
    private Hecho hecho;
    @Setter
    @Getter
    private String descripcion;
    @Setter
    @Getter
    private Boolean aceptada;
    @Setter
    @Getter
    private LocalDateTime fechaDeRevision;
    @Setter
    @Getter
    private Boolean esSpam;
}
