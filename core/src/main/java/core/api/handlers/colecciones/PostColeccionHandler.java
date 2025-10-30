package core.api.handlers.colecciones;

import core.api.DTO.ColeccionDTO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.colecciones.Coleccion;
import core.models.entities.fuentes.Fuente;
import core.models.entities.hecho.Hecho;
import core.models.repository.ColeccionesRepository;
import core.models.repository.FuentesRepository;
import core.models.repository.HechosRepository;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PostColeccionHandler implements Handler {
    private final ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
    private final HechosRepository hechosRepository = HechosRepository.getInstance();
    private final FuentesRepository fuentesRepository = FuentesRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        ColeccionDTO dto  = context.bodyAsClass(ColeccionDTO.class);
        System.out.println("Creando coleccion: " + dto.getTitulo());

        List<Hecho> hechosAsociados = new ArrayList<>();
        List<Hecho> hechosVisibles = new ArrayList<>();
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
                null,
                dto.getTitulo(),
                dto.getDescripcionColeccion(),
                dto.getCriterioDePertenencia(),
                fuentes,
                hechosAsociados,
                hechosVisibles,
                dto.getIdentificadorHandle()
        );

        System.out.println(coleccion);
        coleccionesRepository.add(coleccion);
        context.status(201);
    }
}

