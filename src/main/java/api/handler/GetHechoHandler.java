package api.handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.entities.hecho.Hecho;
import models.repository.HechosRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class GetHechoHandler implements Handler {
    private final HechosRepository repoHechos = HechosRepository.getInstance();


    @Override
    public void handle(@NotNull Context context) throws Exception {
        context.json(repoHechos.obtenerTodas());
    }
}
