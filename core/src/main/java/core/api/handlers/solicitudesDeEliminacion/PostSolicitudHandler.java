package core.api.handlers.solicitudesDeEliminacion;

import core.api.DTO.SolicitudDeEliminacionDTO;
import core.models.agregador.DetectorDeSpam;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.hecho.Hecho;
import core.models.entities.solicitud.SolicitudDeEliminacion;
import core.models.repository.HechosRepository;
import core.models.repository.SolicitudEliminacionRepository;
import org.jetbrains.annotations.NotNull;

public class PostSolicitudHandler implements Handler {
    private final SolicitudEliminacionRepository repoSolicitudes = SolicitudEliminacionRepository.getInstance();
    private final DetectorDeSpam detectorDeSpam = DetectorDeSpam.getInstance();
    @Override
    public void handle(@NotNull Context context) throws Exception {
        SolicitudDeEliminacionDTO dto  = context.bodyAsClass(SolicitudDeEliminacionDTO.class);
        System.out.println("Creando solicitud de eliminación: " + dto.hecho);

        Hecho hecho = HechosRepository.getInstance().getHechoHash(dto.hecho);

        if (hecho == null) {
            context.status(404).result("Hecho con ID " + dto.hecho + " no encontrado");
            return;
        }

        SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(
                hecho,
                dto.descripcion
        );

        System.out.println("Solicitud creada para el hecho " + hecho.getTitulo() + " con la descripción " + solicitud.getDescripcion());
        validarNuevaSolicitud(solicitud);
        if (detectorDeSpam.esSpam(solicitud.getDescripcion()))
        {solicitud.rechazarSolicitud();}
        repoSolicitudes.add(solicitud);
        context.status(201);
    }

    private void validarNuevaSolicitud(SolicitudDeEliminacion solicitud) {
        if (solicitud.getDescripcion() == null) {
            throw new IllegalArgumentException("La descripcion es obligatoria, elegí otro");
        }
    }
}
