package core.models.entities.solicitud;

import core.models.agregador.DetectorDeSpam;
import core.models.repository.SolicitudEliminacionRepository;
import core.models.servicioEstadistica.DTOSolicitudEliminacion;
import core.models.servicioEstadistica.HandlerServicioEstadistica;

import java.io.IOException;

public class HandlerSolicitudDeEliminacion {

    SolicitudEliminacionRepository solicitudEliminacionRepository = SolicitudEliminacionRepository.getInstance();
    DetectorDeSpam detectorDeSpam = DetectorDeSpam.getInstance();
    HandlerServicioEstadistica handlerServicioEstadistica = new HandlerServicioEstadistica();

    public void eliminarSolicitud(Integer codigoSolicitud){
    SolicitudDeEliminacion solicitud = solicitudEliminacionRepository.findById(codigoSolicitud);
    solicitud.aceptarSolicitud();
    }

    public void rechazarSolicitud(Integer codigoSolicitud){
        SolicitudDeEliminacion solicitud = solicitudEliminacionRepository.findById(codigoSolicitud);
        solicitud.rechazarSolicitud();
    }

    public void detectarSpam(Integer codigoSolicitud) throws IOException, InterruptedException {
        SolicitudDeEliminacion solicitudDeEliminacion = solicitudEliminacionRepository.findById(codigoSolicitud);
        Boolean esSpam = DetectorDeSpam.esSpam(solicitudDeEliminacion);
        DTOSolicitudEliminacion dtoSolicitudEliminacion = new DTOSolicitudEliminacion(esSpam);
        handlerServicioEstadistica.enviarSolicitudEliminacion(dtoSolicitudEliminacion);
    }

}
