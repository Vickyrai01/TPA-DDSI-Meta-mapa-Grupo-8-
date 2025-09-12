package cargadorEstatica.handlers;

import cargadorDinamica.model.CargadorDinamico;
import cargadorEstatica.model.CargadorEstatico;
import cargadorEstatica.model.HechoAIntegrarDTO;
import cargadorProxy.model.CargadorProxy;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GetHechosDinamicaHandler implements Handler {
    private final CargadorEstatico cargadorEstatico = CargadorEstatico.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        List<HechoAIntegrarDTO> hechos = cargadorEstatico.extraerHechosAIntegrar();
        context.json(hechos);
    }
}
