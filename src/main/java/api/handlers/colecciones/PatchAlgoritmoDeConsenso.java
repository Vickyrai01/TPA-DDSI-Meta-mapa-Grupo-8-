package api.handlers.colecciones;

import api.dto.ActualizarConsensoDTO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.entities.colecciones.Coleccion;
import models.entities.colecciones.TipoConsenso;
import models.repository.ColeccionesRepository;
import models.repository.FuentesRepository;
import org.jetbrains.annotations.NotNull;


public class PatchAlgoritmoDeConsenso implements Handler {

    private final ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
    private final FuentesRepository fuentesRepository = FuentesRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        int idColeccion = Integer.parseInt(context.pathParam("id"));
        ActualizarConsensoDTO dto = context.bodyAsClass(ActualizarConsensoDTO.class);

        Coleccion coleccion = coleccionesRepository.getColeccion(idColeccion);
        if (coleccion == null) {
            context.status(404).result("Colección no encontrada");
            return;
        }

        coleccion.cambiarAlgoritmoConsenso(dto.tipoConsenso);

        context.status(200).result("Algoritmo de consenso actualizado correctamente");
    }

}
