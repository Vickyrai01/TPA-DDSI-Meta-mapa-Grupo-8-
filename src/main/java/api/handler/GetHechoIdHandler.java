package api.handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.entities.hecho.Hecho;
import models.repository.HechosRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class GetHechoIdHandler implements Handler {
    private final HechosRepository repoHecho;

    public GetHechoIdHandler() {
        this.repoHecho = new HechosRepository();
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {

        Integer idBuscado = context.pathParamAsClass("id", Integer.class).get();
        final Optional<Hecho> resultadoBusqueda = repoHecho.obtenerTodas().stream()
                .filter(m -> m.getId() == idBuscado)
                .findFirst();
        if (resultadoBusqueda.isPresent()) {
            context.status(200).json(resultadoBusqueda.get());
        } else {
            context.status(404);
        }
    }
}