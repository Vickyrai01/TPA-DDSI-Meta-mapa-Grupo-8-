package api.handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.entities.solicitud.SolicitudDeEliminacion;
import models.repository.SolicitudEliminacionRepository;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Optional;

public class PostRecahazarSolicitudHandler implements Handler {
    private final SolicitudEliminacionRepository repo = SolicitudEliminacionRepository.getInstance();

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        int id = ctx.pathParamAsClass("id", Integer.class).get();

        Optional<SolicitudDeEliminacion> solicitudOpt = repo.obtenerTodas().stream()
                .filter(s -> s.getId() == id)
                .findFirst();

        if (solicitudOpt.isEmpty()) {
            ctx.status(404).result("Solicitud no encontrada");
            return;
        }

        SolicitudDeEliminacion solicitud = solicitudOpt.get();
        solicitud.setAceptada(false);
        solicitud.setFechaDeRevision(LocalDateTime.now());

        ctx.status(200).result("Solicitud rechazada");
    }

    //abria que ver si elimina o que a la solicitud!!!
}
