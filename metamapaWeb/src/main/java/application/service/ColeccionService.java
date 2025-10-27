package application.service;

import application.dto.ColeccionDTO;
import application.dto.HechoDTO;
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

    public List<HechoDTO> getHechosDeColeccion(Integer id) {
        return metamapaApi.get()
                .uri(uri -> uri.path("/colecciones/{id}/hechos")
                        .build(id))
                .retrieve()
                .bodyToFlux(HechoDTO.class)
                .collectList()
                .block();
    }

    public ColeccionDTO getById(Integer id){
        return metamapaApi.get()
                .uri(uri -> uri.path("/colecciones/{id}")
                        .build(id))
                .retrieve()
                .bodyToMono(ColeccionDTO.class)
                .block();
    }
}

