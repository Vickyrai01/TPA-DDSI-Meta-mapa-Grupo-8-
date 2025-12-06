package core.api.handlers.fuentes;

import core.models.entities.colecciones.Coleccion;
import core.models.entities.fuentes.Fuente;
import core.models.repository.ColeccionesRepository;
import core.models.repository.FuentesRepository;
import core.models.repository.HechosRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;


public class DeleteFuenteHandler implements Handler {

    private final FuentesRepository fuentesRepository = FuentesRepository.getInstance();
    HechosRepository hechosRepository = HechosRepository.getInstance();
    ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        Integer id = context.pathParamAsClass("id", Integer.class).get();
        hechosRepository.eliminarHechosPorIdFuente(id);
        coleccionesRepository.eliminarFuenteDeTodasLasColecciones(id);

        Optional<Fuente> fuenteOptional = fuentesRepository.obtenerTodas().stream()
                .filter(c -> c.getId() == id)
                .findFirst();

        if (fuenteOptional.isPresent()) {
            fuentesRepository.delete(fuenteOptional.get());
            context.status(200).result("Fuente con ID " + id + " eliminada");
        } else {
            context.status(404).result("Fuente con ID " + id + " no encontrada");
        }
    }

    }

