package models.entities.api.handler;

import api.handlers.colecciones.*;
import api.handlers.hechos.GetHechoHandler;
import api.handlers.hechos.GetHechoIdHandler;
import api.handlers.hechos.PostHechoHandler;
import api.handlers.solicitudesDeEliminacion.*;
import io.javalin.Javalin;
import models.repository.seeders.ColeccionesRepositorySeeder;
import models.repository.seeders.FuentesRepositorySeeder;
import models.repository.seeders.HechosRepositorySeeder;
import models.repository.seeders.SolicitudEliminacioRepositorySeeder;

public class ApplicationApiMETAMAPA {

    public static void main(String[] args) {

        HechosRepositorySeeder hechosRepositorySeeder = HechosRepositorySeeder.getInstance();
        hechosRepositorySeeder.cargarHechosSeeder();

        FuentesRepositorySeeder fuentesRepositorySeeder = FuentesRepositorySeeder.getInstance();
        fuentesRepositorySeeder.cargarFuentesSeeder();

        SolicitudEliminacioRepositorySeeder solicitudEliminacioRepositorySeeder = SolicitudEliminacioRepositorySeeder.getInstance();
        solicitudEliminacioRepositorySeeder.cargarSolicitudDeEliminacionSeeder();

        ColeccionesRepositorySeeder coleccionesRepositorySeeder = ColeccionesRepositorySeeder.getInstance();
        coleccionesRepositorySeeder.cargarColeccionesRepositorySeeder();



        Javalin app = Javalin.create()
                .get("/", ctx -> ctx.result("Hello World"))
                .start(8081);

        app.get("/api/colecciones/{id}/hechos", new GetHechosDeColeccionesHandler());
        app.post("api/solicitudes", new PostSolicitudHandler());
        app.get("api/colecciones", new GetColeccionHandler());
    }



}
