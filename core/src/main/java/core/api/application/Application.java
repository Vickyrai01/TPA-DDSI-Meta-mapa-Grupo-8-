package core.api.application;

import core.observabilidad.RegistroMetricas;
import core.models.repository.seeders.FuentesRepositorySeeder;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        FuentesRepositorySeeder fuentesRepositorySeeder = FuentesRepositorySeeder.getInstance();
        fuentesRepositorySeeder.cargarFuentesSeeder();
        Javalin app = Javalin.create();

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

        // Endpoint de métricas
        app.get("/observabilidad/metrics", ctx -> {
            ctx.json(RegistroMetricas.snapshot());
        });

        // Raíz genérica del core
        app.get("/", ctx -> ctx.result("MetaMapa core API ACTIVA"));

        // Configuramos endpoints públicos y admin sobre la MISMA app
        ApiMetaMapa.configurar(app);       // público
        ApiAdminMetaMapa.configurar(app);  // admin

        // Arrancamos UNA sola vez
        app.start("0.0.0.0", 8081);
    }
}

