package api.configs;

import api.handlers.colecciones.GetColeccionHandler;
import api.handlers.colecciones.GetHechosDeColeccionesHandler;
import api.handlers.hechos.PostHechoHandler;
import api.handlers.solicitudesDeEliminacion.GetSolicitudHandler;
import api.handlers.solicitudesDeEliminacion.PostSolicitudHandler;
import io.javalin.Javalin;

public class ApiMetaMapaConfig {
    public static void configurarEndpoints(Javalin app) {
        app.get("/api/colecciones/{id}/hechos", new GetHechosDeColeccionesHandler());
        app.post("api/solicitudes", new PostSolicitudHandler());
        app.post("/api/hechos/reportar", new PostHechoHandler());
        app.get("api/colecciones", new GetColeccionHandler());
        app.get("api/solicitudes", new GetSolicitudHandler()); //para prueba solo
    }
}
