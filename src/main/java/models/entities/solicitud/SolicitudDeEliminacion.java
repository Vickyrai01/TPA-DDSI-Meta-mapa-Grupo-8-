package models.entities.solicitud;

import lombok.Getter;
import lombok.Setter;
import models.entities.hecho.Hecho;
import models.entities.hecho.Estado;
import models.entities.solicitud.DetectorDeSpam;
import java.time.LocalDateTime;

public class SolicitudDeEliminacion implements DetectorDeSpam {
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

    public void aceptarSolicitud(Hecho hecho) {
        this.aceptada = true;
        hecho.estado = Estado.INACTIVO;
    }

    public void rechazarSolicitud() {
        this.aceptada = false;
    }

    public void rechazarPorSpam() {
        if (esSpam(descripcion)) {
            this.rechazarSolicitud();
        }
    }

    public String toString() {
        return "SolicitudDeEliminacion{" +
                "hecho=" + hecho +
                ", descripcion='" + descripcion + '\'' +
                ", aceptada=" + aceptada +
                ", fechaDeRevision=" + fechaDeRevision +
                '}';
    }

}
