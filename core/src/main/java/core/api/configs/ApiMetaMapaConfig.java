package core.api.configs;

import core.api.handlers.colecciones.GetColeccionHandler;
import core.api.handlers.colecciones.GetHechosDeColeccionCurados;
import core.api.handlers.colecciones.GetHechosDeColeccionesHandler;
import core.api.handlers.hechos.PostHechoHandler;
import core.api.handlers.solicitudesDeEliminacion.GetSolicitudHandler;
import core.api.handlers.solicitudesDeEliminacion.PostSolicitudHandler;
import io.javalin.Javalin;

public class ApiMetaMapaConfig {
    public static void configurarEndpoints(Javalin app) {
        app.get("/core/api/colecciones/{id}/hechos", new GetHechosDeColeccionesHandler());
        app.post("core/api/solicitudes", new PostSolicitudHandler());
        app.get("core/api/colecciones", new GetColeccionHandler());
        app.get("core/api/solicitudes", new GetSolicitudHandler()); //para prueba solo
        app.get("/core/api/colecciones/{id}/{modoVisualizacion}/hechos", new GetHechosDeColeccionCurados());
        app.post("/core/api/hechos/reportar", new PostHechoHandler());
    }
}
