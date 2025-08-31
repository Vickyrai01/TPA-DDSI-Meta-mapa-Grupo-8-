package api.configs;

import api.handlers.colecciones.*;
import api.handlers.hechos.GetHechoHandler;
import api.handlers.hechos.GetHechoIdHandler;
import api.handlers.hechos.PostHechoHandler;
import api.handlers.solicitudesDeEliminacion.*;
import io.javalin.Javalin;

public class ApiAdminMetaMapaConfig {
    public static void configurarEndpoints(Javalin app) {
        app.get("/api/hechos", new GetHechoHandler());
        app.get("/api/hechos/{id}", new GetHechoIdHandler());
        app.post("/api/hechos", new PostHechoHandler());
        app.get("api/solicitudes", new GetSolicitudHandler());
        app.post("api/solicitudes", new PostSolicitudHandler());
        app.get("api/colecciones", new GetColeccionHandler());
        app.get("api/colecciones/{id}", new GetColeccionIdHandler());
        app.post("api/colecciones", new PostColeccionHandler());
        app.delete("api/colecciones/{id}", new DeleteColeccionHandler());
        app.patch("api/colecciones/{id}", new PatchColeccionHandler());
        app.post("/api/solicitudes/{id}/aceptar", new PostAceptarSolicitudHandler());
        app.post("/api/solicitudes/{id}/rechazar", new PostRechazarSolicitudHandler());
        app.get("/api/solicitudes/{id}", new GetSolicitudIdHandler());
        app.patch("api/colecciones/{id}/fuentes/agregar", new PatchAgregarFuentesColeccionHandler());
        app.patch("api/colecciones/{id}/fuentes/eliminar", new PatchEliminarFuentesColeccionHandler());
        app.patch("api/colecciones/{id}/consenso/modificar", new PatchAlgoritmoDeConsenso());
    }
}
