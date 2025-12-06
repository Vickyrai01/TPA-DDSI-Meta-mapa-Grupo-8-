package core.api.handlers.fuentes;

import core.models.repository.FuentesRepository;
import core.models.repository.SolicitudEliminacionRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class GetFuentesHandler implements Handler {
    private final FuentesRepository fuentesRepository = FuentesRepository.getInstance();

    @Override
    public void handle(@NotNull Context ctx) {
        var dtos = fuentesRepository.obtenerTodas()
                .stream()
                .map(core.api.DTO.FuenteDTO::from)
                .toList();
        ctx.json(dtos);
    }

}
