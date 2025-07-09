package api.dto;

import java.time.LocalDateTime;

public class SolicitudDeEliminacionDTO {
        public int id;
        public int hecho;
        public String descripcion;
        public Boolean aceptada;
        public LocalDateTime fechaDeRevision;

        public SolicitudDeEliminacionDTO() {}
    }

