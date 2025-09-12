package cargadorDINAMICO.application;

import api.configs.ApiAdminMetaMapaConfig;
import cargadorDINAMICO.configs.ApiCargadorDinamicoConfig;
import io.javalin.Javalin;
import models.repository.seeders.ColeccionesRepositorySeeder;
import models.repository.seeders.FuentesRepositorySeeder;
import models.repository.seeders.HechosRepositorySeeder;
import models.repository.seeders.SolicitudEliminacioRepositorySeeder;

public class ApiCargadorDinamico {
        public static void main(String[] args) {

            Javalin app = Javalin.create()
                    .get("/", ctx -> ctx.result("API Cargador Dinámico ACTIVA"))
                    .start(8081);

            ApiCargadorDinamicoConfig.configurarEndpoints(app);
        }
    }
