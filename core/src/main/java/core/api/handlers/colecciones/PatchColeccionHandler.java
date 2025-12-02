package core.api.handlers.colecciones;

import core.api.DTO.ActualizoColeccionDTO;
import core.api.DTO.criterio.CriterioDTO;
import core.models.entities.colecciones.*;
import core.models.entities.fuentes.Fuente;
import io.javalin.http.Context;
import io.javalin.http.Handler;
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
            List<Criterio> criterios = new ArrayList<>();
            for (CriterioDTO criterioDTO : (List<CriterioDTO>) dto.criterioDePertenencia) {

            }
            coleccion.setCriterioDePertenencia(criterios);
            coleccionesRepository.update(coleccion);
        }
        /*
        if (dto.algoritmoConsenso != null) {
            coleccion.setAlgoritmoConsenso(
                    switch(dto.algoritmoConsenso.toUpperCase()) {
                        case "ABSOLUTO", "ABSOLUTA" -> new StrategyAbsoluta();
                        case "MAYORIA_SIMPLE", "MAYORIA-SIMPLE" -> new StrategyMayoriaSimple();
                        case "MULTIPLES_MENCIONES", "MULTIPLES-MENCIONES" -> new StrategyMultiplesMenciones();
                        default -> throw new IllegalArgumentException("Tipo de algoritmo desconocido: " + dto.algoritmoConsenso.toUpperCase());
                    }
            );
        }
        */

        if (dto.algoritmoConsenso != null) {
            coleccion.cambiarAlgoritmoConsenso(
                    switch(dto.algoritmoConsenso.toUpperCase()) {
                        case "ABSOLUTO", "ABSOLUTA" -> TipoConsenso.ABSOLUTO;
                        case "MAYORIA_SIMPLE", "MAYORIA-SIMPLE" -> TipoConsenso.MAYORIA_SIMPLE;
                        case "MULTIPLES_MENCIONES", "MULTIPLES-MENCIONES" -> TipoConsenso.MULTIPLES_MENCIONES;
                        case "SIN" -> null;
                        default -> throw new IllegalArgumentException("Tipo de algoritmo desconocido: " + dto.algoritmoConsenso.toUpperCase());
                    }
            );
            coleccionesRepository.update(coleccion);
        }

        if (dto.modoDeNavegacion != null) {
            coleccion.setModoDeNavegacion(
                    switch(dto.modoDeNavegacion.toUpperCase()){
                        case "CURADA" -> ModoDeNavegacion.CURADA;
                        case "IRRESTRICTA" -> ModoDeNavegacion.IRRESTRICTA;
                        default -> throw new IllegalArgumentException("Modo de navegacion desconocido: " + dto.modoDeNavegacion.toUpperCase());
                    }
            );
            coleccionesRepository.update(coleccion);
        }

        // Actualizar fuentes si viene el campo
        if (dto.fuentes != null) {
            // Validar que existan todas las fuentes antes de actualizar
            for (Integer idFuente : dto.fuentes) {
                Fuente fuente = core.models.repository.FuentesRepository.getInstance().getFuente(idFuente);
                if (fuente == null) {
                    context.status(404).result("Fuente con ID " + idFuente + " no encontrada");
                    return;
                }
            }
            coleccionesRepository.actualizarFuentesDeColeccion(id, dto.fuentes);
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
