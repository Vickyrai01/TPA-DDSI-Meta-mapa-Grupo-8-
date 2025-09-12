package cargadorDinamica;

import cargadorProxy.configs.ApiCargadorProxyConfig;
import cargadorProxy.handlers.GetHechosProxyHandler;
import cargadorProxy.handlers.PostFuenteHandler;
import io.javalin.Javalin;

public class Application {
    public static void main(String[] args) {

        Javalin app = Javalin.create()
                .get("/", ctx -> ctx.result("API Cargador Dinámico ACTIVA"))
                .start(8082);

        configurarEndpoints(app);
    }

    private static void configurarEndpoints(Javalin app) {
        app.get("fuentesDinamicas/obtenerHechos", new GetHechosDinamicaHandler());
    }
}
