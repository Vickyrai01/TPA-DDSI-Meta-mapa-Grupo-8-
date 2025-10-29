package application.service;

import application.dto.ColeccionDTO;
import application.dto.HechoDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

@Service
public class HechoService {
    private final WebClient metamapaApi = WebClient.create("http://localhost:8081/core/api");

    public List<HechoDTO> getAll() {
        return metamapaApi.get()
                .uri("/hechos")
                .retrieve()
                .bodyToFlux(HechoDTO.class)
                .collectList()
                .block();
    }
}