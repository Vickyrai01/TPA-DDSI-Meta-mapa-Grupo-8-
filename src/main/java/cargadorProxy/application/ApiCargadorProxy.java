package cargadorProxy.application;

import cargadorProxy.configs.ApiCargadorProxyConfig;
import io.javalin.Javalin;

public class ApiCargadorProxy {
        public static void main(String[] args) {

            Javalin app = Javalin.create()
                    .get("/", ctx -> ctx.result("API Cargador Dinámico ACTIVA"))
                    .start(8081);

            ApiCargadorProxyConfig.configurarEndpoints(app);
        }
    }
