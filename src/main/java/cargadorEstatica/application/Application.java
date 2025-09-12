package cargadorEstatica.application;

import cargadorDinamica.GetHechosDinamicaHandler;
import cargadorEstatica.handlers.PostFuenteNuevaEstatica;
import io.javalin.Javalin;

public class Application {
    public static void main(String[] args) {

        Javalin app = Javalin.create()
                .get("/", ctx -> ctx.result("API Cargador Estatico ACTIVA"))
                .start(8083);

        configurarEndpoints(app);
    }

    private static void configurarEndpoints(Javalin app) {
        app.get("fuentesEstaticas/obtenerHechos", new GetHechosDinamicaHandler());
        app.post("fuentesEstaticas/agregarFuente", new PostFuenteNuevaEstatica());
    }
}

