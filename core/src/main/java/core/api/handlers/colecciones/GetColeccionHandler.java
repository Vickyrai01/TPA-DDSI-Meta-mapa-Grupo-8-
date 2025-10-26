package core.api.handlers.colecciones;

import core.models.repository.ColeccionesRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class GetColeccionHandler implements Handler{


    private final ColeccionesRepository repoColecciones = ColeccionesRepository.getInstance();
    @Override
    public void handle(@NotNull Context ctx) {
        var dtos = repoColecciones.listarColeccionesDTOConCantidadHechos();
        ctx.status(200).json(dtos);
    }
}
