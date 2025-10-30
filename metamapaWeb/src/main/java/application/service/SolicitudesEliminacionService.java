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

    public boolean rechazar(Integer id) {
        try {
            var resp = metamapaApiADMIN.post()
                    .uri(uri -> uri.path("/solicitudes/{id}/rechazar").build(id)) // baseUrl: http://localhost:8082/core/api
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return resp != null && resp.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean aceptar(Integer id) {
        try {
            var resp = metamapaApiADMIN.post()
                    .uri(uri -> uri.path("/solicitudes/{id}/aceptar").build(id)) // baseUrl: http://localhost:8082/core/api
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return resp != null && resp.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }
}
