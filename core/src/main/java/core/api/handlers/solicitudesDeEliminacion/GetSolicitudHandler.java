package core.api.handlers.solicitudesDeEliminacion;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.repository.SolicitudEliminacionRepository;
import org.jetbrains.annotations.NotNull;

public class GetSolicitudHandler implements Handler {
    private final SolicitudEliminacionRepository repoSolicitudes = SolicitudEliminacionRepository.getInstance();


    @Override
    public  void handle(@NotNull Context context) throws Exception {
        context.json(repoSolicitudes.obtenerTodas());
    }


}
