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
        app.get("/admin/core/api/hechos", new GetHechoHandler());
        app.get("/admin/core/api/hechos/{id}", new GetHechoIdHandler());
        app.post("/admin/core/api/hechos", new PostHechoHandler());
        app.get("/admin/core/api/solicitudes", new GetSolicitudHandler());
        app.post("/admin/core/api/solicitudes", new PostSolicitudHandler());
        app.get("/admin/core/api/colecciones", new GetColeccionHandler());
        app.post("/admin/core/api/colecciones", new PostColeccionHandler());
        app.get("/admin/core/api/colecciones/{id}", new GetColeccionIdHandler());
        app.delete("/admin/core/api/colecciones/{id}", new DeleteColeccionHandler());
        app.patch("/admin/core/api/colecciones/{id}", new PatchColeccionHandler());
        app.post("/admin/core/api/solicitudes/{id}/aceptar", new PostAceptarSolicitudHandler());
        app.post("/admin/core/api/solicitudes/{id}/rechazar", new PostRechazarSolicitudHandler());
        app.get("/admin/core/api/solicitudes/{id}", new GetSolicitudIdHandler());
        app.patch("/admin/core/api/colecciones/{id}/fuentes/agregar", new PatchAgregarFuentesColeccionHandler());
        app.patch("/admin/core/api/colecciones/{id}/fuentes/eliminar", new PatchEliminarFuentesColeccionHandler());
        app.patch("/admin/core/api/colecciones/{id}/consenso/modificar", new PatchAlgoritmoDeConsenso());
        app.get("/admin/core/api/fuentes", new GetFuentesHandler());

        app.patch("/admin/core/api/hechos/{hash}", new PatchHechoHandler());
        app.delete("/admin/core/api/hechos/{hash}", new DeleteHechoHandler());

        app.post("/admin/core/api/fuentes", new PostFuenteHandler());
        app.post("/admin/core/api/fuentes/{id}/csv", new PostFuenteCSVHandler());
        app.post("/admin/core/api/fuentes/{id}/eliminar", new DeleteFuenteHandler());

        app.post("/admin/core/api/ejecutarServicio", new EjecutarServicioHandler());
    }
}
