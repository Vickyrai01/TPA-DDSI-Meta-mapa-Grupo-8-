package application.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

@Service
public class ColeccionService {
    private final WebClient metamapaApi;

    public ColeccionService(WebClient metamapaApi) {
        this.metamapaApi = metamapaApi;
    }

    // Obtener todas las colecciones del core
    public List<Map<String, Object>> getAll() {
        return metamapaApi.get()
                .uri("/colecciones")
                .retrieve()
                .bodyToFlux(Map.class)
                .collectList()
                .block(); // <- bloqueamos para usarlo en MVC clásico (no reactivo)
    }
}

