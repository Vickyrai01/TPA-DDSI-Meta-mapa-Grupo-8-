package core.api.handlers.colecciones;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.repository.ColeccionesRepository;
import org.jetbrains.annotations.NotNull;

public class GetColeccionHandler implements Handler{

    private final ColeccionesRepository repoColecciones = ColeccionesRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        context.json(repoColecciones.obtenerTodas());
    }
}
