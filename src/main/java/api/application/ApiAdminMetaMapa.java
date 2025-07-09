package api.application;

import api.configs.ApiAdminMetaMapaConfig;
import io.javalin.Javalin;
import models.repository.seeders.ColeccionesRepositorySeeder;
import models.repository.seeders.FuentesRepositorySeeder;
import models.repository.seeders.HechosRepositorySeeder;
import models.repository.seeders.SolicitudEliminacioRepositorySeeder;

public class ApiAdminMetaMapa {

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
                .get("/", ctx -> ctx.result("API ADMINISTRATIVA META MAPA ACTIVA"))
                .start(8080);

        ApiAdminMetaMapaConfig.configurarEndpoints(app);
    }
}
