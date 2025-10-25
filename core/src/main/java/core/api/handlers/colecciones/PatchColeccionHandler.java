package core.api.handlers.colecciones;

import core.api.DTO.ActualizoColeccionDTO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.colecciones.Coleccion;
import core.models.entities.colecciones.criterios.Criterio;
import core.models.entities.hecho.Hecho;
import core.models.repository.ColeccionesRepository;
import core.models.repository.HechosRepository;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatchColeccionHandler implements Handler {
    private final ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
    private final HechosRepository hechosRepository = HechosRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        int id = context.pathParamAsClass("id", Integer.class).get();

        Optional<Coleccion> coleccionOpt = coleccionesRepository.obtenerTodas().stream()
                .filter(c -> c.getId() == id)
                .findFirst();

        if (coleccionOpt.isEmpty()) {
            context.status(404).result("Colección no encontrada");
            return;
        }

        Coleccion coleccion = coleccionOpt.get();
        ActualizoColeccionDTO dto = context.bodyAsClass(ActualizoColeccionDTO.class);

        if (dto.titulo != null) {
            coleccion.setTitulo(dto.titulo);
            coleccionesRepository.update(coleccion);
        }

        if (dto.descripcionColeccion != null) {
            coleccion.setDescripcionColeccion(dto.descripcionColeccion);
            coleccionesRepository.update(coleccion);
        }

        if (dto .criterioDePertenencia != null) {
            coleccion.setCriterioDePertenencia((List<Criterio>) dto.criterioDePertenencia);
            coleccionesRepository.update(coleccion);
        }


        //si quiero agregar hechos tengo que copiar los previos dado que sobrescribe
        if (dto.hechos != null) {
            List<Hecho> hechos = new ArrayList<>();
            for (Integer idHecho : dto.hechos) {
                Hecho hecho = hechosRepository.getHecho(idHecho);
                if (hecho == null) {
                    context.status(404).result("Hecho con ID " + idHecho + " no encontrado");
                    return;
                }
                hechos.add(hecho);
            }
            coleccion.setHechos(hechos);
            coleccionesRepository.update(coleccion);
        }

        context.status(200).result("Colección actualizada correctamente");
    }
}
