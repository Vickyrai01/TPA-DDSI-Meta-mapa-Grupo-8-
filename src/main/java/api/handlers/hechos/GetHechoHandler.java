package api.handlers.hechos;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.repository.HechosRepository;
import org.jetbrains.annotations.NotNull;

public class GetHechoHandler implements Handler {
    private final HechosRepository repoHechos = HechosRepository.getInstance();


    @Override
    public void handle(@NotNull Context context) throws Exception {
        context.json(repoHechos.obtenerTodas());
    }
}
