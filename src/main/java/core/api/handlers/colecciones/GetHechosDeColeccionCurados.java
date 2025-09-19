package core.api.handlers.colecciones;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.colecciones.Coleccion;
import core.models.entities.colecciones.ModoDeNavegacion;
import core.models.entities.hecho.Hecho;
import core.models.repository.ColeccionesRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;


public class GetHechosDeColeccionCurados implements Handler {

    private final ColeccionesRepository repoColecciones = ColeccionesRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {

        Integer idBuscado = context.pathParamAsClass("id", Integer.class).get();
        Optional<Coleccion> resultadoBusqueda = repoColecciones.obtenerTodas().stream()
                .filter(c -> c.getId() == idBuscado)
                .findFirst();


        String modoVisualizacion = context.pathParam("modoVisualizacion");
        ModoDeNavegacion modoDeNavegacion = ModoDeNavegacion.valueOf(modoVisualizacion.toUpperCase());
        if (resultadoBusqueda.isPresent()) {

            resultadoBusqueda.get().modificarModoNavegacion(modoDeNavegacion);
            List<Hecho> hechosFiltrados = resultadoBusqueda.get().getHechosVisibles();

            context.status(200).json(hechosFiltrados);  //solo hechos :)
        } else {
            context.status(404).result("Colección no encontrada con ID: " + idBuscado);
        }

    }

}
