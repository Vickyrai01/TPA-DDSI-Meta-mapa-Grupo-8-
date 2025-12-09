package core.api.configs;

import core.api.handlers.colecciones.GetColeccionHandler;
import core.api.handlers.colecciones.GetColeccionIdHandler;
import core.api.handlers.colecciones.GetHechosDeColeccionCurados;
import core.api.handlers.colecciones.GetHechosDeColeccionesHandler;
import core.api.handlers.hechos.GetCategoriasHandler;
import core.api.handlers.hechos.GetHechoIdHandler;
import core.api.handlers.hechos.PostHechoHandler;
import core.api.handlers.solicitudesDeEliminacion.GetSolicitudHandler;
import core.api.handlers.solicitudesDeEliminacion.PostSolicitudHandler;
import io.javalin.Javalin;

public class ApiMetaMapaConfig {
    public static void configurarEndpoints(Javalin app) {
        app.get("/public/core/api/colecciones/{id}/hechos", new GetHechosDeColeccionesHandler());
        app.post("/public/core/api/solicitudes", new PostSolicitudHandler());
        app.get("/public/core/api/colecciones", new GetColeccionHandler());
        app.get("/public/core/api/colecciones/{id}", new GetColeccionIdHandler());
        app.get("/public/core/api/solicitudes", new GetSolicitudHandler()); //para prueba solo
        app.get("/public/core/api/colecciones/{id}/{modoVisualizacion}/hechos", new GetHechosDeColeccionCurados());
        app.post("/public/core/api/hechos/reportar", new PostHechoHandler());
        app.get("/public/core/api/categorias", new GetCategoriasHandler());
        app.get("/public/core/api/ping", ctx -> {
            System.out.println(">>> /ping recibido");
            ctx.result("pong");
        });

    }
}
