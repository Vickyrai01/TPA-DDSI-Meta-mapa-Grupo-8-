package api.handlers.hechos;

import api.dto.HechoDTO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.entities.hecho.Coordenadas;
import models.entities.hecho.Hecho;
import models.repository.HechosRepository;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public class PostHechoHandler implements Handler {
    private final HechosRepository repoHechos = HechosRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        HechoDTO dto = context.bodyAsClass(HechoDTO.class);
        System.out.println("Creando hecho: " + context.body());


        Hecho hecho = new Hecho();
        hecho.setId(dto.getId());
        hecho.setTitulo(dto.getTitulo());

        Coordenadas coordenadas = new Coordenadas(dto.getLatitud(), dto.getLongitud());

        hecho.setUbicacion(coordenadas);
        hecho.setFechaSuceso(dto.getFechaSuceso());
        hecho.setFechaCarga(LocalDate.now());
        hecho.setDescripcion(dto.getDescripcion());

        validarNuevoHecho(hecho);
        repoHechos.add(hecho);
        context.status(201);
    }


    private void validarNuevoHecho(Hecho hecho) {
        if (hecho.getTitulo() == null) {
            throw new IllegalArgumentException("El nombre es obligatorio, elegí otro");
        }
    }
}
