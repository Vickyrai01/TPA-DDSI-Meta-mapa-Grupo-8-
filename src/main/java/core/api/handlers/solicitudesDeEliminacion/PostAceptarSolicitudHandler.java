package core.api.handlers.solicitudesDeEliminacion;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.solicitud.SolicitudDeEliminacion;
import core.models.repository.HechosRepository;
import core.models.repository.SolicitudEliminacionRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PostAceptarSolicitudHandler implements Handler {
    private final SolicitudEliminacionRepository repo = SolicitudEliminacionRepository.getInstance();
    private final HechosRepository repoHechos = HechosRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        int id = context.pathParamAsClass("id", Integer.class).get();

        Optional<SolicitudDeEliminacion> solicitudOpt = repo.obtenerTodas().stream()
                .filter(s -> s.getId() == id)
                .findFirst();


        if (solicitudOpt.isEmpty()) {
            context.status(404).result("Solicitud no encontrada");
            return;
        }

        SolicitudDeEliminacion solicitud = solicitudOpt.get();
        solicitud.aceptarSolicitud();

        repoHechos.delete(solicitud.getHecho());

        context.status(200).result("Solicitud aprobada");
    }
}
