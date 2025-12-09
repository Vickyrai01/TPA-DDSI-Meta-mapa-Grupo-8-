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

    public static void configurar(Javalin app) {

        // Podés dejar un root específico para la parte pública
        app.get("/public", ctx -> ctx.result("API MetaMapa PÚBLICA ACTIVA"));

        // Todo lo que antes configurabas en 8081, ahora sobre la misma app
        ApiMetaMapaConfig.configurarEndpoints(app);
    }
}
