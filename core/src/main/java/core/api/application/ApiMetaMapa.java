package core.api.application;

import core.api.configs.ApiMetaMapaConfig;
import io.javalin.Javalin;
import core.models.repository.seeders.ColeccionesRepositorySeeder;
import core.models.repository.seeders.FuentesRepositorySeeder;
import core.models.repository.seeders.HechosRepositorySeeder;
import core.models.repository.seeders.SolicitudEliminacioRepositorySeeder;
import utils.DBUtils;

import javax.persistence.EntityManager;

public class ApiMetaMapa {

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
                .get("/", ctx -> ctx.result("API MetaMapa ACTIVA"))
                .start(8081);

        app.before(ctx -> {
            ctx.header("Access-Control-Allow-Origin", "http://localhost:8080");
            ctx.header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type");
        });
        app.options("/*", ctx -> {
            ctx.header("Access-Control-Allow-Origin", "http://localhost:8080");
            ctx.header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type");
            ctx.status(204);
        });

        ApiMetaMapaConfig.configurarEndpoints(app);

        EntityManager em = DBUtils.getEntityManager();
        DBUtils.comenzarTransaccion(em);

        //em.persist();
        DBUtils.commit(em);

    }
}
