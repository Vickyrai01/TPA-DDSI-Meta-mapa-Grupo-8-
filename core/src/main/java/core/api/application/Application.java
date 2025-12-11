package core.api.application;

import core.api.graphql.GraphQLProvider;
import core.models.repository.ColeccionesRepository;
import core.models.repository.FuentesRepository;
import core.models.repository.HechosRepository;
import core.models.repository.seeders.ColeccionesRepositorySeeder;
import core.models.repository.seeders.FuentesRepositorySeeder;
import core.models.repository.seeders.HechosRepositorySeeder;
import core.models.repository.seeders.SolicitudEliminacioRepositorySeeder;
import core.observabilidad.PrometheusExporter;
import core.observabilidad.RegistroMetricas;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        HechosRepositorySeeder hechosRepositorySeeder = HechosRepositorySeeder.getInstance();
        hechosRepositorySeeder.cargarHechosSeeder();

        FuentesRepositorySeeder fuentesRepositorySeeder = FuentesRepositorySeeder.getInstance();
        fuentesRepositorySeeder.cargarFuentesSeeder();

        SolicitudEliminacioRepositorySeeder solicitudEliminacioRepositorySeeder = SolicitudEliminacioRepositorySeeder.getInstance();
        solicitudEliminacioRepositorySeeder.cargarSolicitudDeEliminacionSeeder();

        ColeccionesRepositorySeeder coleccionesRepositorySeeder = ColeccionesRepositorySeeder.getInstance();
        coleccionesRepositorySeeder.cargarColeccionesRepositorySeeder();


        HechosRepository hechosRepo = HechosRepository.getInstance();
        ColeccionesRepository colRepo = ColeccionesRepository.getInstance();
        FuentesRepository fuentesRepo = FuentesRepository.getInstance();

        GraphQLProvider graphQLProvider = new GraphQLProvider(hechosRepo, colRepo, fuentesRepo);
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public"); // carpeta en el classpath: src/main/resources/public
        });

        // BEFORE → logs + correlationId + tiempo inicio + métrica de request
        app.before(ctx -> {
            RegistroMetricas.sumPeticiones();

            // Correlation ID
            String correlationId = ctx.header("X-Correlation-Id");
            if (correlationId == null || correlationId.isBlank()) {
                correlationId = UUID.randomUUID().toString();
            }
            MDC.put("correlationId", correlationId);

            // Guardar tiempo inicio
            ctx.attribute("startTimeNs", System.nanoTime());

            // Log de request
            log.info("REQ correlationId={} method={} path={} ip={} query={}",
                    correlationId,
                    ctx.method(),
                    ctx.path(),
                    ctx.req().getRemoteAddr(),
                    ctx.queryString()
            );
        });

        // AFTER → logs + duración + limpiar MDC
        app.after(ctx -> {
            Long start = ctx.attribute("startTimeNs");
            long durationMs = -1;

            if (start != null) {
                durationMs = (System.nanoTime() - start) / 1_000_000;
                RegistroMetricas.sumTiempoPeticion(durationMs);
            }

            log.info("RES correlationId={} method={} path={} status={} durationMs={}",
                    MDC.get("correlationId"),
                    ctx.method(),
                    ctx.path(),
                    ctx.status(),
                    durationMs
            );

            MDC.clear();
        });

        // Excepciones → log + métrica de error
        app.exception(Exception.class, (e, ctx) -> {
            RegistroMetricas.sumErrores();

            log.error("ERROR correlationId={} method={} path={} msg={}",
                    MDC.get("correlationId"),
                    ctx.method(),
                    ctx.path(),
                    e.getMessage(),
                    e
            );

            ctx.status(500).result("Error interno");
            MDC.clear();
        });

        // Raíz genérica del core
        app.get("/", ctx -> ctx.result("MetaMapa core API ACTIVA"));

        // Endpoint de métricas
        app.get("/metricas", ctx -> ctx.json(RegistroMetricas.snapshot()));
        app.get("/metrics", ctx -> {
            String body = PrometheusExporter.export(RegistroMetricas.snapshot());
            ctx.contentType("text/plain; charset=utf-8");
            ctx.result(body);
        });


        app.post("/graphql", ctx -> {
            System.out.println("BODY RECIBIDO: " + ctx.body());
            Map<String, Object> body = ctx.bodyAsClass(Map.class);

            String query = (String) body.get("query");
            Map<String, Object> variables = (Map<String, Object>) body.getOrDefault("variables", Map.of());

            Map<String, Object> result = graphQLProvider.execute(query, variables);
            ctx.json(result);
        });

        app.get("/playground", ctx -> ctx.redirect("/graphiql.html"));


        // Configuramos endpoints públicos y admin sobre la MISMA app
        ApiMetaMapa.configurar(app);       // público
        ApiAdminMetaMapa.configurar(app, graphQLProvider);  // admin

        // Arrancamos UNA sola vez
        app.start("0.0.0.0", 8081);
    }
}
