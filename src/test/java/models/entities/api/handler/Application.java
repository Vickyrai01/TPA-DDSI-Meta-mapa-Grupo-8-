package models.entities.api.handler;

import api.handler.*;
import io.javalin.Javalin;
import models.repository.seeders.ColeccionesRepositorySeeder;
import models.repository.seeders.HechosRepositorySeeder;
import models.repository.seeders.SolicitudEliminacioRepositorySeeder;

public class Application {

    public static void main(String[] args) {

        HechosRepositorySeeder hechosRepositorySeeder = HechosRepositorySeeder.getInstance();
        hechosRepositorySeeder.cargarHechosSeeder();

        SolicitudEliminacioRepositorySeeder solicitudEliminacioRepositorySeeder = SolicitudEliminacioRepositorySeeder.getInstance();
        solicitudEliminacioRepositorySeeder.cargarSolicitudDeEliminacionSeeder();

        ColeccionesRepositorySeeder coleccionesRepositorySeeder = ColeccionesRepositorySeeder.getInstance();
        coleccionesRepositorySeeder.cargarColeccionesRepositorySeeder();

        Javalin app = Javalin.create()
                .get("/", ctx -> ctx.result("Hello World"))
                .start(8080);

        app.get("/api/hechos", new GetHechoHandler());
        app.get("/api/hechos/{id}", new GetHechoIdHandler());
        app.post("/api/hechos", new PostHechoHandler());
        app.get("api/solicitudes", new GetSolicitudHandler());
        app.post("api/solicitudes", new PostSolicitudHandler());
        app.get("/api/colecciones/{id}/hechos", new GetHechosDeColeccionesHandler());
        app.get("api/colecciones", new GetColeccionHandler());
        app.get("api/colecciones/{id}", new GetColeccionIdHandler());
        app.post("api/colecciones", new PostColeccionHandler());
    }



}
