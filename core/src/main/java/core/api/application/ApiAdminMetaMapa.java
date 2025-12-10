package core.api.application;

import core.api.configs.ApiAdminMetaMapaConfig;
import core.api.graphql.GraphQLProvider;
import core.models.repository.ColeccionesRepository;
import core.models.repository.FuentesRepository;
import core.models.repository.HechosRepository;
import io.javalin.Javalin;
import core.models.repository.seeders.ColeccionesRepositorySeeder;
import core.models.repository.seeders.FuentesRepositorySeeder;
import core.models.repository.seeders.HechosRepositorySeeder;
import core.models.repository.seeders.SolicitudEliminacioRepositorySeeder;

import java.util.Map;

public class ApiAdminMetaMapa {

    public static void main(String[] args) {
        /*
        HechosRepositorySeeder hechosRepositorySeeder = HechosRepositorySeeder.getInstance();
        hechosRepositorySeeder.cargarHechosSeeder();

        FuentesRepositorySeeder fuentesRepositorySeeder = FuentesRepositorySeeder.getInstance();
        fuentesRepositorySeeder.cargarFuentesSeeder();

        SolicitudEliminacioRepositorySeeder solicitudEliminacioRepositorySeeder = SolicitudEliminacioRepositorySeeder.getInstance();
        solicitudEliminacioRepositorySeeder.cargarSolicitudDeEliminacionSeeder();

        ColeccionesRepositorySeeder coleccionesRepositorySeeder = ColeccionesRepositorySeeder.getInstance();
        coleccionesRepositorySeeder.cargarColeccionesRepositorySeeder();

         */

        FuentesRepositorySeeder fuentesRepositorySeeder = FuentesRepositorySeeder.getInstance();
        fuentesRepositorySeeder.cargarFuentesSeeder();

        HechosRepository hechosRepo = HechosRepository.getInstance();
        ColeccionesRepository colRepo = ColeccionesRepository.getInstance();
        FuentesRepository fuentesRepo = FuentesRepository.getInstance();

        GraphQLProvider graphQLProvider = new GraphQLProvider(hechosRepo, colRepo, fuentesRepo);

        Javalin app = Javalin.create()
                .get("/", ctx -> ctx.result("API ADMINISTRATIVA MetaMapa ACTIVA"))
                .start(8082);

        // NUEVO: endpoint GraphQL
        app.post("/graphql", ctx -> {
            Map<String, Object> body = ctx.bodyAsClass(Map.class);

            String query = (String) body.get("query");
            Map<String, Object> variables = (Map<String, Object>) body.getOrDefault("variables", Map.of());

            Map<String, Object> result = graphQLProvider.execute(query, variables);
            ctx.json(result);
        });

        ApiAdminMetaMapaConfig.configurarEndpoints(app);
    }
}
