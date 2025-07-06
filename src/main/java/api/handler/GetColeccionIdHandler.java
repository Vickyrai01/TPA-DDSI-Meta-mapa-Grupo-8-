package api.handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.entities.colecciones.Coleccion;
import models.entities.hecho.Hecho;
import models.repository.ColeccionesRepository;
import models.repository.HechosRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class GetColeccionIdHandler implements Handler {

    private final ColeccionesRepository repoColecciones = ColeccionesRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {

        Integer idBuscado = context.pathParamAsClass("id", Integer.class).get();
        final Optional<Coleccion> resultadoBusqueda = repoColecciones.obtenerTodas().stream()
                .filter(m -> m.getId() == idBuscado)
                .findFirst();
        if (resultadoBusqueda.isPresent()) {
            context.status(200).json(resultadoBusqueda.get());
        } else {
            context.status(404);
        }

    }
}
