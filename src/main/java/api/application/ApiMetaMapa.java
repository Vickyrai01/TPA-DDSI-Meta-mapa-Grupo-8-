package api.application;

import api.configs.ApiAdminMetaMapaConfig;
import api.configs.ApiMetaMapaConfig;
import io.javalin.Javalin;
import models.repository.seeders.ColeccionesRepositorySeeder;
import models.repository.seeders.FuentesRepositorySeeder;
import models.repository.seeders.HechosRepositorySeeder;
import models.repository.seeders.SolicitudEliminacioRepositorySeeder;

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
                .start(8082);

        ApiMetaMapaConfig.configurarEndpoints(app);
    }
}
