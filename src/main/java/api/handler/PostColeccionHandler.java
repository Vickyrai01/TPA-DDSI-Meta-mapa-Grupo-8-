package api.handler;

import api.clasesResponse.ColeccionResponse;
import api.clasesResponse.SolicitudDeEliminacionResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.entities.colecciones.Coleccion;
import models.entities.colecciones.CriterioDePertenencia;
import models.entities.fuentes.Fuente;
import models.entities.hecho.Hecho;
import models.entities.solicitud.SolicitudDeEliminacion;
import models.repository.ColeccionesRepository;
import models.repository.HechosRepository;
import models.repository.SolicitudEliminacionRepository;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PostColeccionHandler implements Handler {
    private final ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
    private final HechosRepository hechosRepository = HechosRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        ColeccionResponse dto  = context.bodyAsClass(ColeccionResponse.class);
        System.out.println("Creando coleccion: " + dto.getTitulo());

        List<Hecho> hechosAsociados = new ArrayList<>();

        for (Integer idHecho : dto.getHechos()) {
            Hecho hecho = hechosRepository.getHecho(idHecho);
            if (hecho != null) {
                hechosAsociados.add(hecho);
            } else {
                context.status(404).result("Hecho con ID " + idHecho + " no encontrado");
                return;
            }
        }

        Coleccion coleccion = new Coleccion(
                dto.getId(),
                dto.getTitulo(),
                dto.getDescripcionColeccion(),
                dto.getFuente(),
                dto.getCriterioDePertenencia(),
                hechosAsociados,
                dto.getIdentificadorHandle()
        );

        System.out.println(coleccion);
        coleccionesRepository.add(coleccion);
        context.status(201);
    }
}

