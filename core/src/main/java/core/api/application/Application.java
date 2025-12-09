package core.api.application;

import core.models.repository.seeders.FuentesRepositorySeeder;
import io.javalin.Javalin;

public class Application {

    public static void main(String[] args) {
        FuentesRepositorySeeder fuentesRepositorySeeder = FuentesRepositorySeeder.getInstance();
        fuentesRepositorySeeder.cargarFuentesSeeder();
        Javalin app = Javalin.create();

        // Raíz genérica del core
        app.get("/", ctx -> ctx.result("MetaMapa core API ACTIVA"));

        // Configuramos endpoints públicos y admin sobre la MISMA app
        ApiMetaMapa.configurar(app);       // público
        ApiAdminMetaMapa.configurar(app);  // admin

        // Arrancamos UNA sola vez
        app.start("0.0.0.0", 8081);
    }
}

