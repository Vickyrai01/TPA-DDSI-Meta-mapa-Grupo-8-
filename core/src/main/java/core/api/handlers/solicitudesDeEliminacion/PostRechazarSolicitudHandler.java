package core.api.handlers.solicitudesDeEliminacion;

import core.models.repository.HechosRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.solicitud.SolicitudDeEliminacion;
import core.models.repository.SolicitudEliminacionRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PostRechazarSolicitudHandler implements Handler {
    private final SolicitudEliminacionRepository repo = SolicitudEliminacionRepository.getInstance();
    private final HechosRepository repoHechos = HechosRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        int id = context.pathParamAsClass("id", Integer.class).get();

        SolicitudDeEliminacion solicitud = repo.findById(id);
        if (solicitud == null) {
            context.status(404).result("Solicitud no encontrada");
            return;
        }

        solicitud.rechazarSolicitud();
        repo.update(solicitud);

        repoHechos.update(solicitud.getHecho());
        context.status(200).result("Solicitud rechazada");
    }

    //abria que ver si elimina o que a la solicitud!!!
}
