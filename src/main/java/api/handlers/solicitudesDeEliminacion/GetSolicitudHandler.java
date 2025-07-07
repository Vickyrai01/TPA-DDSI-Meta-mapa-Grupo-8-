package api.handlers.solicitudesDeEliminacion;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.repository.SolicitudEliminacionRepository;
import org.jetbrains.annotations.NotNull;

public class GetSolicitudHandler implements Handler {
    private final SolicitudEliminacionRepository repoSolicitudes = SolicitudEliminacionRepository.getInstance();


    @Override
    public void handle(@NotNull Context context) throws Exception {
        context.json(SolicitudEliminacionRepository.obtenerTodas());
    }


}
