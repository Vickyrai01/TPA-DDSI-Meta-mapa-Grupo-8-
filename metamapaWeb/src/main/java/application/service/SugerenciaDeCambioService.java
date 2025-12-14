package application.service;

import application.dto.SolicitudDeEliminacionDTO;
import application.dto.SugerenciaConHechoDTO;
import application.dto.SugerenciaDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SugerenciaDeCambioService {
    private final WebClient metamapaApiADMIN;

    public SugerenciaDeCambioService(RutasProperties props) {
        this.metamapaApiADMIN = WebClient.create(props.getAdminBaseUrl());
    }

    public List<SugerenciaConHechoDTO> getAll() {
        return metamapaApiADMIN.get()
                .uri("/sugerencias")
                .retrieve()
                .bodyToFlux(SugerenciaConHechoDTO.class)
                .filter(dto -> dto.aceptada() == null)
                .collectList()
                .block();
    }

    public boolean rechazar(Integer id) {
        try {
            var resp = metamapaApiADMIN.post()
                    .uri(uri -> uri.path("/sugerencias/{id}/rechazar").build(id)) // baseUrl: http://localhost:8082/core/api
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
                    .uri(uri -> uri.path("/sugerencias/{id}/aceptar").build(id)) // baseUrl: http://localhost:8082/core/api
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return resp != null && resp.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean crear(SugerenciaDTO sugerencia) {
        try {
            var resp = metamapaApiADMIN.post()
                    .uri("/sugerencias")
                    .bodyValue(sugerencia)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return resp != null && resp.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            System.err.println("Error creando sugerencia de cambio: " + e.getMessage());
            return false;
        }
    }
}
