package cargadorProxy.configs;

import cargadorProxy.handlers.GetHechosProxyHandler;
import cargadorProxy.handlers.PostFuenteHandler;
import io.javalin.Javalin;

public class ApiCargadorProxyConfig {
    public static void configurarEndpoints(Javalin app) {
        app.get("fuentesDinamicas/obtenerHechos", new GetHechosProxyHandler());
        app.post("fuentesDinamicas/agregarFuente", new PostFuenteHandler());
    }
}
