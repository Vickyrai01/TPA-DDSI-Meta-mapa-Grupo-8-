package models.entities.api.handler;

import api.handler.GetHechoHandler;
import api.handler.GetHechoIdHandler;
import api.handler.PostHechoHandler;
import io.javalin.Javalin;
import models.entities.hecho.Hecho;
import models.repository.HechosRepository;

public class Application {

    public static void main(String[] args) {

        Javalin app = Javalin.create()
                .get("/", ctx -> ctx.result("Hello World"))
                .start(8081);

        app.get("/api/hechos", new GetHechoHandler());
        app.get("/api/hechos/{id}", new GetHechoIdHandler());
        app.post("/api/hechos", new PostHechoHandler());


    }



}
