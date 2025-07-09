package api.handlers.colecciones;

import api.dto.ActualizoColeccionDTO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.entities.colecciones.Coleccion;
import models.entities.colecciones.criterios.Criterio;
import models.entities.hecho.Hecho;
import models.repository.ColeccionesRepository;
import models.repository.HechosRepository;
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
        }

        if (dto.descripcionColeccion != null) {
            coleccion.setDescripcionColeccion(dto.descripcionColeccion);
        }

        if (dto .criterioDePertenencia != null) {
            coleccion.setCriterioDePertenencia((List<Criterio>) dto.criterioDePertenencia);
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
        }

        context.status(200).result("Colección actualizada correctamente");
    }
}
