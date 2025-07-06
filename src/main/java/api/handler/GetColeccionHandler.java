package api.handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.entities.colecciones.Coleccion;
import models.entities.hecho.Hecho;
import models.repository.ColeccionesRepository;
import models.repository.HechosRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class GetColeccionHandler implements Handler{

    private final ColeccionesRepository repoColecciones = ColeccionesRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        context.json(repoColecciones.obtenerTodas());
    }
}
