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

public class GetColeccionesHandler implements Handler {

    private final ColeccionesRepository repoColecciones = ColeccionesRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {

        Integer idBuscado = context.pathParamAsClass("id", Integer.class).get();
        Optional<Coleccion> resultadoBusqueda = repoColecciones.obtenerTodas().stream()
                .filter(c -> c.getId() == idBuscado)
                .findFirst();

        if (resultadoBusqueda.isPresent()) {
            List<Hecho> hechos = resultadoBusqueda.get().getHechos();
            context.status(200).json(hechos);  //solo hechos :)
        } else {
            context.status(404).result("Colección no encontrada con ID: " + idBuscado);
        }
    }
    }
