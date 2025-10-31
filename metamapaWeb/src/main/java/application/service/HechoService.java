package application.service;

import application.dto.ColeccionDTO;
import application.dto.HechoDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HechoService {
    private final WebClient metamapaApi = WebClient.create("http://localhost:8081/core/api");

    // API administrativa (edición/borrado)
    private final WebClient adminApi = WebClient.builder()
            .baseUrl("http://localhost:8082/core/api")
            .build();

    public List<HechoDTO> getAll() {
        return adminApi.get()
                .uri("/hechos")
                .retrieve()
                .bodyToFlux(HechoDTO.class)
                .collectList()
                .block();
    }
    // EDITAR (admin 8082) — PATCH /core/api/hechos/{hash}
    public boolean patchByHash(String hash, String nombre, String descripcion, List<String> etiquetas) {
        if (hash == null || hash.isBlank()) return false;

        Map<String, Object> body = new HashMap<>();
        if (nombre != null) body.put("nombre", nombre);
        if (descripcion != null) body.put("descripcion", descripcion);
        if (etiquetas != null) body.put("etiquetas", etiquetas);

        // Si no hay cambios, lo consideramos OK
        if (body.isEmpty()) return true;

        try {
            adminApi.patch()
                    .uri("/hechos/{hash}", hash)
                    .bodyValue(body)
                    .retrieve()
                    .toBodilessEntity()
                    .block(); // 204 esperado
            return true;
        } catch (WebClientResponseException e) {
            System.err.println("PATCH /hechos/" + hash + " error " + e.getStatusCode() + ": " + e.getResponseBodyAsString());
            return false;
        } catch (Exception e) {
            System.err.println("PATCH /hechos/" + hash + " error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteByHash(String hash) {
        if (hash == null || hash.isBlank()) return false;

        try {
            adminApi.delete()
                    .uri("/hechos/{hash}", hash)
                    .retrieve()
                    .toBodilessEntity()
                    .block(); // 204 esperado
            return true;
        } catch (WebClientResponseException e) {
            System.err.println("DELETE /hechos/" + hash + " error " + e.getStatusCode() + ": " + e.getResponseBodyAsString());
            return false;
        } catch (Exception e) {
            System.err.println("DELETE /hechos/" + hash + " error: " + e.getMessage());
            return false;
        }
    }
}