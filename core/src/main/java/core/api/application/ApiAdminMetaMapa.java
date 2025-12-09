package core.api.application;

import core.api.configs.ApiAdminMetaMapaConfig;
import io.javalin.Javalin;
import core.models.repository.seeders.ColeccionesRepositorySeeder;
import core.models.repository.seeders.FuentesRepositorySeeder;
import core.models.repository.seeders.HechosRepositorySeeder;
import core.models.repository.seeders.SolicitudEliminacioRepositorySeeder;

public class ApiAdminMetaMapa {

    public static void configurar(Javalin app) {

        // Root "admin" para chequear rápido desde el navegador
        app.get("/admin", ctx -> ctx.result("API ADMINISTRATIVA MetaMapa ACTIVA"));

        // Endpoints administrativos (antes en el server de 8082)
        ApiAdminMetaMapaConfig.configurarEndpoints(app);
    }
}
