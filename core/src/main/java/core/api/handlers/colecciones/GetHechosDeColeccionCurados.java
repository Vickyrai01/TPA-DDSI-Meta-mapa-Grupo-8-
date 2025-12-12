package core.api.handlers.colecciones;

import core.api.DTO.FiltroHechoDTO;
import core.api.DTO.HechoResumenDTO;
import core.api.utils.FiltroHechosMapper;
import core.models.entities.colecciones.criterios.Criterio;
import core.models.entities.colecciones.criterios.FiltradorColecciones;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.colecciones.Coleccion;
import core.models.entities.colecciones.ModoDeNavegacion;
import core.models.entities.hecho.Hecho;
import core.models.repository.ColeccionesRepository;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GetHechosDeColeccionCurados implements Handler {

    private final ColeccionesRepository repoColecciones = ColeccionesRepository.getInstance();
    // podés inyectarlo también si ya lo tenés en algún lado
    private final GetHechosDeColeccionesHandler handlerIrrestricta = new GetHechosDeColeccionesHandler();

    @Override
    public void handle(@NotNull Context context) throws Exception {

        Integer idBuscado = context.pathParamAsClass("id", Integer.class).get();
        String modoVisualizacion = context.pathParam("modoVisualizacion");

        FiltroHechoDTO filtro = FiltroHechosMapper.extraerFiltroDeContext(context);

        // ─────────────────────────────────────────────
        // 1) IRRESTRICTA → delega al handler que ya funciona
        // ─────────────────────────────────────────────
        if ("IRRESTRICTA".equalsIgnoreCase(modoVisualizacion)) {
            //handlerIrrestricta.handle(context);

            var opt = repoColecciones.findByIdFetchHechosYContribuyente(idBuscado); // <<-- NUEVO
            if (opt.isEmpty()) {
                context.status(404).result("Colección no encontrada con ID: " + idBuscado);
                return;
            }

            Coleccion coleccion = opt.get();
            List<Hecho> hechosParaFiltrar = coleccion.getHechos(); // <-- la lista original
            List<Hecho> hechosFiltrados = FiltradorColecciones.getInstance()
                    .filtrarHechosPorDTO(hechosParaFiltrar, filtro);   // <-- filtrás esa lista

            var respuesta = hechosFiltrados.stream()
                    .map(HechoResumenDTO::from)
                    .toList();

            context.status(200).json(respuesta);


            return;
        }

        // ─────────────────────────────────────────────
        // 2) CURADA → devolver hechos visibles
        // ─────────────────────────────────────────────
        if ("CURADA".equalsIgnoreCase(modoVisualizacion)) {

            // Usamos el mismo repo que el otro handler (ya te trae hechos cargados)
            var opt = repoColecciones.findByIdFetchHechosVisiblesYContribuyente(idBuscado); // <<-- NUEVO
            if (opt.isEmpty()) {
                context.status(404).result("Colección no encontrada con ID: " + idBuscado);
                return;
            }

            Coleccion coleccion = opt.get();
            //List<Hecho> hechosFiltrados = FiltradorColecciones.getInstance().filtrarColeccion(coleccion, criterios);
            if(coleccion.getAlgoritmoConsenso() == null){
                context.status(400).result("La coleccion no tiene algoritmo de consenso definido.");
                return;
            }

            // devuelve DTOs para no tocar relaciones LAZY de Hecho!!

            List<Hecho> hechosParaFiltrar = coleccion.getHechosVisibles(); // <-- la lista de visibles
            List<Hecho> hechosFiltrados = FiltradorColecciones.getInstance()
                    .filtrarHechosPorDTO(hechosParaFiltrar, filtro);

            var respuesta = hechosFiltrados.stream()
                    .map(HechoResumenDTO::from)
                    .toList();

            context.status(200).json(respuesta);
            return;
        }

        // ─────────────────────────────────────────────
        // 3) Modo inválido
        // ─────────────────────────────────────────────
        context.status(400).result("modoVisualizacion inválido. Use IRRESTRICTA o CURADA.");
    }
}
