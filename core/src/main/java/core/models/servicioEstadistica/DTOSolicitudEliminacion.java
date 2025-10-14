package core.models.servicioEstadistica;

public class DTOSolicitudEliminacion {
    Boolean fueSpam; //1 si SÍ fue SPAM, 0 si NO.

    public DTOSolicitudEliminacion(){}

    public DTOSolicitudEliminacion(Boolean fueSpam) {
        this.fueSpam = fueSpam;
    }

    public Boolean getFueSpam() {
        return fueSpam;
    }

    public void setFueSpam(Boolean fueSpam) {
        this.fueSpam = fueSpam;
    }
}
