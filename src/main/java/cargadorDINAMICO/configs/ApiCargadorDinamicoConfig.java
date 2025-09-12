package cargadorDINAMICO.configs;

import cargadorDINAMICO.handlers.GetHechosDinamicoHandler;
import cargadorDINAMICO.handlers.PostFuenteHandler;
import io.javalin.Javalin;

public class ApiCargadorDinamicoConfig {
    public static void configurarEndpoints(Javalin app) {
        app.get("fuentesDinamicas/obtenerHechos", new GetHechosDinamicoHandler());
        app.post("fuentesDinamicas/agregarFuente", new PostFuenteHandler());
    }
}
