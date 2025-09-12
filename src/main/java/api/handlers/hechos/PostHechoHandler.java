package api.handlers.hechos;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import api.dto.HechoAIntegrarDTO;
import models.repository.DinamicaRepository;
import org.jetbrains.annotations.NotNull;

public class PostHechoHandler implements Handler {
    private final DinamicaRepository repoDinamicos = DinamicaRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        HechoAIntegrarDTO dto = context.bodyAsClass(HechoAIntegrarDTO.class);
        System.out.println("Creando hecho: " + context.body());


        HechoAIntegrarDTO hechoDTO = new HechoAIntegrarDTO(
                dto.getTitulo(),
                dto.getDescripcion(),
                dto.getCategoria(),
                dto.getLatitud(),
                dto.getLongitud(),
                dto.getFechaSuceso()
        );

        validarNuevoHecho(hechoDTO);

        repoDinamicos.add(hechoDTO);
        context.status(201);
    }


    private void validarNuevoHecho(HechoAIntegrarDTO hecho) {
        if (hecho.getTitulo() == null) {
            throw new IllegalArgumentException("El nombre es obligatorio, elegí otro");
        }
    }
}
