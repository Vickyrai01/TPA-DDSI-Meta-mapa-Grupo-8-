package cargadorDinamica;

import cargadorDinamica.model.CargadorDinamico;
import cargadorDinamica.model.HechoAIntegrarDTO;
import cargadorProxy.model.CargadorProxy;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GetHechosDinamicaHandler implements Handler {

    private final CargadorDinamico cargadorDinamico = CargadorDinamico.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        List<HechoAIntegrarDTO> hechos = cargadorDinamico.extraerHechosAIntegrar();
        context.json(hechos);
    }
}
