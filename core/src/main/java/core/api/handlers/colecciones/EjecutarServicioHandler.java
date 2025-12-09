package core.api.handlers.colecciones;

import core.models.agregador.SchedulerAgregador;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class EjecutarServicioHandler implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        SchedulerAgregador agregador = new SchedulerAgregador(false);

        agregador.verificarNuevosHechos();
    }
}