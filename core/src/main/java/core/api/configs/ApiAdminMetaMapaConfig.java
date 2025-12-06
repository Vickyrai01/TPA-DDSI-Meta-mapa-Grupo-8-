package core.api.configs;

import core.api.handlers.colecciones.*;
import core.api.handlers.fuentes.DeleteFuenteHandler;
import core.api.handlers.fuentes.GetFuentesHandler;
import core.api.handlers.fuentes.PostFuenteCSVHandler;
import core.api.handlers.fuentes.PostFuenteHandler;
import core.api.handlers.hechos.*;
import core.api.handlers.solicitudesDeEliminacion.*;
import io.javalin.Javalin;

public class ApiAdminMetaMapaConfig {
    public static void configurarEndpoints(Javalin app) {
        app.get("/core/api/hechos", new GetHechoHandler());
        app.get("/core/api/hechos/{id}", new GetHechoIdHandler());
        app.post("/core/api/hechos", new PostHechoHandler());
        app.get("core/api/solicitudes", new GetSolicitudHandler());
        app.post("core/api/solicitudes", new PostSolicitudHandler());
        app.get("core/api/colecciones", new GetColeccionHandler());
        app.post("core/api/colecciones", new PostColeccionHandler());
        app.get("core/api/colecciones/{id}", new GetColeccionIdHandler());
        app.delete("core/api/colecciones/{id}", new DeleteColeccionHandler());
        app.patch("core/api/colecciones/{id}", new PatchColeccionHandler());
        app.post("/core/api/solicitudes/{id}/aceptar", new PostAceptarSolicitudHandler());
        app.post("/core/api/solicitudes/{id}/rechazar", new PostRechazarSolicitudHandler());
        app.get("/core/api/solicitudes/{id}", new GetSolicitudIdHandler());
        app.patch("core/api/colecciones/{id}/fuentes/agregar", new PatchAgregarFuentesColeccionHandler());
        app.patch("core/api/colecciones/{id}/fuentes/eliminar", new PatchEliminarFuentesColeccionHandler());
        app.patch("core/api/colecciones/{id}/consenso/modificar", new PatchAlgoritmoDeConsenso());
        app.get("/core/api/fuentes", new GetFuentesHandler());

        app.patch("/core/api/hechos/{hash}", new PatchHechoHandler());
        app.delete("/core/api/hechos/{hash}", new DeleteHechoHandler());

        app.post("/core/api/fuentes", new PostFuenteHandler());
        app.post("/core/api/fuentes/{id}/csv", new PostFuenteCSVHandler());
        app.post("core/api/fuentes/{id}/eliminar", new DeleteFuenteHandler());
    }
}
