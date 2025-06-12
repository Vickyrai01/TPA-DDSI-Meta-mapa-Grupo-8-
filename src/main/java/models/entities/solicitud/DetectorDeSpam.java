package models.entities.solicitud;

public interface DetectorDeSpam {

    default boolean esSpam(String solicitud) {return true;}
}
