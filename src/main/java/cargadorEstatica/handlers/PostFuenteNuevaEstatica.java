package cargadorEstatica.handlers;

import cargadorProxy.model.Fuente;
import cargadorProxy.repository.RepositoryFuentes;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class PostFuenteNuevaEstatica implements Handler {

    private final RepositoryFuentes repoFuentes = RepositoryFuentes.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        Fuente fuente = context.bodyAsClass(Fuente.class);
        repoFuentes.agregarFuente(fuente);
        context.status(201).result("Fuente guardada correctamente");
    }
}
