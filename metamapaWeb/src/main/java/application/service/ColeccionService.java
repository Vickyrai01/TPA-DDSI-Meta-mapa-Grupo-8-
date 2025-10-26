package application.service;

import application.dto.ColeccionDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

@Service
public class ColeccionService {
    private final WebClient metamapaApi = WebClient.create("http://localhost:8081/core/api");

    // Obtener todas las colecciones del core
    public List<ColeccionDTO> getAll() {
        return metamapaApi.get()
                .uri("/colecciones")
                .retrieve()
                .bodyToFlux(ColeccionDTO.class)
                .collectList()
                .block();
    }
}

