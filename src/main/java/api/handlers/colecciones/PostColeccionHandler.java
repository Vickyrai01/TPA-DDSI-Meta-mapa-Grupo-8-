package api.handlers.colecciones;

import api.DTO.ColeccionResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.entities.colecciones.Coleccion;
import models.entities.fuentes.Fuente;
import models.entities.hecho.Hecho;
import models.repository.ColeccionesRepository;
import models.repository.FuentesRepository;
import models.repository.HechosRepository;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PostColeccionHandler implements Handler {
    private final ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
    private final HechosRepository hechosRepository = HechosRepository.getInstance();
    private final FuentesRepository fuentesRepository = FuentesRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        ColeccionResponse dto  = context.bodyAsClass(ColeccionResponse.class);
        System.out.println("Creando coleccion: " + dto.getTitulo());

        List<Hecho> hechosAsociados = new ArrayList<>();
        List<Fuente> fuentes = new ArrayList<>();

        for (Integer idHecho : dto.getHechos()) {
            Hecho hecho = hechosRepository.getHecho(idHecho);
            if (hecho != null) {
                hechosAsociados.add(hecho);
            } else {
                context.status(404).result("Hecho con ID " + idHecho + " no encontrado");
                return;
            }
        }

        for (Integer idFuente : dto.getFuente()) {
            Fuente fuente = fuentesRepository.getFuente(idFuente);
            if (fuente != null) {
                fuentes.add(fuente);
            } else {
                context.status(404).result("Fuente con ID " + idFuente + " no encontrado");
                return;
            }
        }

        Coleccion coleccion = new Coleccion(
                dto.getId(),
                dto.getTitulo(),
                dto.getDescripcionColeccion(),
                dto.getCriterioDePertenencia(),
                fuentes,
                hechosAsociados,
                dto.getIdentificadorHandle()
        );

        System.out.println(coleccion);
        coleccionesRepository.add(coleccion);
        context.status(201);
    }
}

