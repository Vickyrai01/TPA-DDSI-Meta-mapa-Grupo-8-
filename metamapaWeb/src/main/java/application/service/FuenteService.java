package application.service;

import application.dto.ColeccionDTO;
import application.dto.FuenteDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class FuenteService {
    private final WebClient metamapaApi = WebClient.create("http://localhost:8081/core/api");
    private final WebClient metamapaApiADMIN = WebClient.create("http://localhost:8082/core/api");

    // Obtener todas las colecciones del core
    public List<FuenteDTO> getAll() {
        return metamapaApiADMIN.get()
                .uri("/fuentes")
                .retrieve()
                .bodyToFlux(FuenteDTO.class)
                .collectList()
                .block();
    }

}
