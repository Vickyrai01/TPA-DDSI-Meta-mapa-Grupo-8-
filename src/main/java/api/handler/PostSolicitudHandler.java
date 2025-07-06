package api.handler;

import api.clasesResponse.SolicitudDeEliminacionResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.entities.hecho.Hecho;
import models.entities.solicitud.SolicitudDeEliminacion;
import models.repository.HechosRepository;
import models.repository.SolicitudEliminacionRepository;
import org.jetbrains.annotations.NotNull;

public class PostSolicitudHandler implements Handler {
    private final SolicitudEliminacionRepository repoSolicitudes = SolicitudEliminacionRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        SolicitudDeEliminacionResponse dto  = context.bodyAsClass(SolicitudDeEliminacionResponse.class);
        System.out.println("Creando solicitud de eliminación: " + dto.hecho);

        Hecho hecho = HechosRepository.getInstance().getHecho(dto.hecho);

        if (hecho == null) {
            context.status(404).result("Hecho con ID " + dto.hecho + " no encontrado");
            return;
        }

        SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(
                hecho,
                dto.descripcion,
                dto.aceptada,
                dto.fechaDeRevision
        );

        System.out.println(solicitud);
        validarNuevaSolicitud(solicitud);
        repoSolicitudes.add(solicitud);
        context.status(201);
    }

    private void validarNuevaSolicitud(SolicitudDeEliminacion solicitud) {
        if (solicitud.getDescripcion() == null) {
            throw new IllegalArgumentException("La descripcion es obligatoria, elegí otro");
        }
    }
}
