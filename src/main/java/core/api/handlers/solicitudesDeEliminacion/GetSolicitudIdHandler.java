package core.api.handlers.solicitudesDeEliminacion;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.solicitud.SolicitudDeEliminacion;
import core.models.repository.SolicitudEliminacionRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class GetSolicitudIdHandler implements Handler {
    private final SolicitudEliminacionRepository repoSolicitud = SolicitudEliminacionRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {

        Integer idBuscado = context.pathParamAsClass("id", Integer.class).get();
        final Optional<SolicitudDeEliminacion> resultadoBusqueda = repoSolicitud.obtenerTodas().stream()
                .filter(m -> m.getId() == idBuscado)
                .findFirst();
        if (resultadoBusqueda.isPresent()) {
            context.status(200).json(resultadoBusqueda.get());
        } else {
            context.status(404);
        }
    }

}
