package api.handlers.colecciones;

import api.dto.ActualizarFuentesColeccionDTO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.entities.colecciones.Coleccion;
import models.entities.fuentes.Fuente;
import models.repository.ColeccionesRepository;
import models.repository.FuentesRepository;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PatchAgregarFuentesColeccionHandler implements Handler
{
   private final ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
    private final FuentesRepository fuentesRepository = FuentesRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        int idColeccion = Integer.parseInt(context.pathParam("id"));
        ActualizarFuentesColeccionDTO dto = context.bodyAsClass(ActualizarFuentesColeccionDTO.class);

        Coleccion coleccion = coleccionesRepository.getColeccion(idColeccion);
        if (coleccion == null) {
            context.status(404).result("Colección no encontrada");
            return;
        }

        List<Fuente> fuentesActuales = new ArrayList<>(coleccion.getFuentes());

        for (Integer idFuente : dto.fuentes) {
            Fuente fuente = fuentesRepository.getFuente(idFuente);
            if (fuente != null) {
                if (!fuentesActuales.contains(fuente)) {
                    coleccion.agregarFuente(fuente);
                }
            } else {
                context.status(404).result("Fuente con ID " + idFuente + " no encontrada");
                return;
            }
        }

        context.status(200).result("Fuentes agregadas correctamente");
    }

    private void enviarFuenteAlCargador(Fuente fuente){
        
    }
}

