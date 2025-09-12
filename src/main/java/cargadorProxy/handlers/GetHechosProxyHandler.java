package cargadorProxy.handlers;

import cargadorProxy.model.CargadorProxy;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import cargadorProxy.model.HechoAIntegrarDTO;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GetHechosProxyHandler implements Handler {
    private final CargadorProxy cargadorProxy = CargadorProxy.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        List<HechoAIntegrarDTO> hechos = cargadorProxy.extraerHechosAIntegrar();
        context.json(hechos);
    }
}
