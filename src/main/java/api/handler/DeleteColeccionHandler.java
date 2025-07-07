package api.handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.entities.colecciones.Coleccion;
import models.repository.ColeccionesRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class DeleteColeccionHandler implements Handler {

    private final ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        Integer id = context.pathParamAsClass("id", Integer.class).get();

        Optional<Coleccion> coleccionOpt = coleccionesRepository.obtenerTodas().stream()
                .filter(c -> c.getId() == id)
                .findFirst();

        if (coleccionOpt.isPresent()) {
            coleccionesRepository.delete(coleccionOpt.get());
            context.status(200).result("Colección con ID " + id + " eliminada");
        } else {
            context.status(404).result("Colección con ID " + id + " no encontrada");
        }
    }
}
