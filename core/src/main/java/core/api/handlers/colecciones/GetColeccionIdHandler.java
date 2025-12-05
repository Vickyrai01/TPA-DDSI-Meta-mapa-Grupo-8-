package core.api.handlers.colecciones;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.colecciones.Coleccion;
import core.models.repository.ColeccionesRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class GetColeccionIdHandler implements Handler {

    private final ColeccionesRepository repoColecciones = ColeccionesRepository.getInstance();

    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        Integer idBuscado = ctx.pathParamAsClass("id", Integer.class).get();

        var optDto = repoColecciones.obtenerColeccionDTOConCantidadHechos(idBuscado);

        if (optDto == null) {
            ctx.status(404).result("Colección no encontrada con ID: " + idBuscado);
            return;
        }

        // Devuelve el DTO completo ya armado
        ctx.status(200).json(optDto);
    }
}
