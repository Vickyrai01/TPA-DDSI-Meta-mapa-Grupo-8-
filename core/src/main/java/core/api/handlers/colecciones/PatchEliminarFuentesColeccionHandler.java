package core.api.handlers.colecciones;

import core.api.DTO.ActualizarFuentesColeccionDTO;
import core.api.DTO.FuenteDTO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.colecciones.Coleccion;
import core.models.entities.fuentes.Fuente;
import core.models.repository.ColeccionesRepository;
import core.models.repository.FuentesRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class PatchEliminarFuentesColeccionHandler implements Handler {

    private final ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
    private final FuentesRepository fuentesRepository = FuentesRepository.getInstance();

    @Override
    public void handle(@NotNull Context ctx) {
        int idColeccion = Integer.parseInt(ctx.pathParam("id"));
        ActualizarFuentesColeccionDTO dto = ctx.bodyAsClass(ActualizarFuentesColeccionDTO.class);

        // 1) Traer colección con FUENTES fetch-eadas
        var opt = coleccionesRepository.findByIdFetchFuentes(idColeccion);
        if (opt.isEmpty()) {
            ctx.status(404).result("Colección no encontrada");
            return;
        }
        Coleccion coleccion = opt.get();
        if (coleccion.getFuentes() == null) {
            // nada que eliminar
            ctx.status(200).json(java.util.Collections.emptyList());
            return;
        }

        if (dto == null || dto.fuentes == null || dto.fuentes.isEmpty()) {
            ctx.status(400).result("Lista de fuentes vacía o inválida");
            return;
        }

        // 2) Validar las fuentes a eliminar y construir set de IDs válidos
        java.util.Set<Integer> idsAEliminar = new java.util.HashSet<>();
        for (Integer idFuente : dto.fuentes) {
            if (idFuente == null) continue;
            if (fuentesRepository.getFuente(idFuente) == null) {
                ctx.status(404).result("Fuente con ID " + idFuente + " no encontrada");
                return;
            }
            idsAEliminar.add(idFuente);
        }

        // 3) Eliminar por ID
        boolean huboCambios = coleccion.getFuentes().removeIf(f -> idsAEliminar.contains(f.getId()));

        // 4) Persistir una sola vez
        if (huboCambios) {
            coleccionesRepository.update(coleccion);
        }

        // 5) Responder: lista actualizada como DTOs
        var respuesta = coleccion.getFuentes().stream()
                .map(FuenteDTO::from)
                .toList();

        ctx.status(200).json(respuesta);
    }
}
