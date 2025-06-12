package api.handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.entities.hecho.Hecho;
import models.repository.HechosRepository;
import org.jetbrains.annotations.NotNull;

public class PostHechoHandler implements Handler {
    private final HechosRepository repoHecho;

    public PostHechoHandler() {
        this.repoHecho = new HechosRepository();
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String bodyString = context.body();
        Hecho hecho = context.bodyAsClass(Hecho.class);
        System.out.println("Creando hecho: " + bodyString);
        System.out.println(hecho);
        validarNuevoHecho(hecho);
        context.status(201);
    }

    private void validarNuevoHecho(Hecho hecho) {
        if (hecho.getTitulo() == null) {
            throw new IllegalArgumentException("El nombre es obligatorio, elegí otro");
        }
    }
}
