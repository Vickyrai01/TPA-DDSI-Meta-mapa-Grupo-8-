package api.handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.entities.solicitud.SolicitudDeEliminacion;
import models.repository.SolicitudEliminacionRepository;
import org.jetbrains.annotations.NotNull;

public class PostSolicitudHandler implements Handler {
    private final SolicitudEliminacionRepository repoSolicitudes = SolicitudEliminacionRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String bodyString = context.body();
        SolicitudDeEliminacion solicitud = context.bodyAsClass(SolicitudDeEliminacion.class);
        System.out.println("Creando solicitud de eliminación: " + bodyString);
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
