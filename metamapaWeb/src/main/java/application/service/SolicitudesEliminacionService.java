package application.service;

import application.dto.FuenteDTO;
import application.dto.SolicitudDeEliminacionDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class SolicitudesEliminacionService {
    private final WebClient metamapaApi = WebClient.create("http://localhost:8081/core/api");
    private final WebClient metamapaApiADMIN = WebClient.create("http://localhost:8082/core/api");

    // Obtener todas las colecciones del core
    public List<SolicitudDeEliminacionDTO> getAll() {
        return metamapaApiADMIN.get()
                .uri("/solicitudes")
                .retrieve()
                .bodyToFlux(SolicitudDeEliminacionDTO.class)
                .collectList()
                .block();
    }
}
