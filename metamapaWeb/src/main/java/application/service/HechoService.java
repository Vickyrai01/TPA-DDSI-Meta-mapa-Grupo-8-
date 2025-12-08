package application.service;

import application.dto.HechoDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class HechoService {
    private final WebClient metamapaApi = WebClient.create("http://localhost:8081/core/api");
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

    // NEW: Filtra hechos por coleccion y parámetros
    public List<HechoDTO> filtrarHechosDeColeccion(
            Integer coleccionId,
            String modo,
            String titulo,
            String descripcion,
            String etiqueta,
            String categoria,
            String fechaDesdeSuceso,
            String fechaHastaSuceso,
            String fechaDesdeCarga,
            String fechaHastaCarga,
            String provincia,
            Boolean soloMultimedia
    ) {
        // 1. Obtené los hechos originales de la colección y modo (CURADA/IRRESTRICTA)
        List<HechoDTO> hechos = getHechosDeColeccionConModo(coleccionId, modo);

        // 2. Filtrado en memoria (adaptar los getters a tu DTO)
        Stream<HechoDTO> stream = hechos.stream();

        if (titulo != null && !titulo.isBlank())
            stream = stream.filter(h -> h.nombre() != null && h.nombre().toLowerCase().contains(titulo.toLowerCase()));

        if (descripcion != null && !descripcion.isBlank())
            stream = stream.filter(h -> h.descripcion() != null && h.descripcion().toLowerCase().contains(descripcion.toLowerCase()));

        if (etiqueta != null && !etiqueta.isBlank())
            stream = stream.filter(h -> h.etiquetas() != null && h.etiquetas().contains(etiqueta));

        if (categoria != null && !categoria.isBlank())
            stream = stream.filter(h -> h.categorias() != null && h.categorias().contains(categoria));

        if (fechaDesdeSuceso != null && !fechaDesdeSuceso.isBlank())
            stream = stream.filter(h -> h.fechaSuceso() != null &&
                    !h.fechaSuceso().isBefore(LocalDate.parse(fechaDesdeSuceso)));
        if (fechaHastaSuceso != null && !fechaHastaSuceso.isBlank())
            stream = stream.filter(h -> h.fechaSuceso() != null &&
                    !h.fechaSuceso().isAfter(LocalDate.parse(fechaHastaSuceso)));

        if (fechaDesdeCarga != null && !fechaDesdeCarga.isBlank())
            stream = stream.filter(h -> h.fechaCarga() != null &&
                    !h.fechaCarga().isBefore(LocalDate.parse(fechaDesdeCarga)));
        if (fechaHastaCarga != null && !fechaHastaCarga.isBlank())
            stream = stream.filter(h -> h.fechaCarga() != null &&
                    !h.fechaCarga().isAfter(LocalDate.parse(fechaHastaCarga)));
        /*
        if (provincia != null && !provincia.isBlank())
            stream = stream.filter(h -> h.provincia() != null &&
                    h.provincia().equalsIgnoreCase(provincia));
        */
        if (Boolean.TRUE.equals(soloMultimedia))
            stream = stream.filter(h -> h.multimedia() != null && !h.multimedia().isEmpty());

        return stream.toList();
    }

    // Helper: obtené hechos de una colección y modo (llama a tu backend)
    public List<HechoDTO> getHechosDeColeccionConModo(Integer id, String modo) {
        // Si tu endpoint soporta filtro por modo, lo usás acá:
        String url = String.format("/colecciones/%d/%s/hechos", id, modo != null ? modo : "CURADA");
        try {
            return metamapaApi.get()
                    .uri(url)
                    .retrieve()
                    .bodyToFlux(HechoDTO.class)
                    .collectList()
                    .block();
        } catch (WebClientResponseException e) {
            System.err.println("API hechos coleccion error " + e.getStatusCode() + ": " + e.getResponseBodyAsString());
            return List.of();
        } catch (Exception e) {
            System.err.println("API hechos coleccion error: " + e.getMessage());
            return List.of();
        }
    }

    // (Opcional, si implementás selects dinámicos)
    public List<String> getCategorias() {
        // ejemplo: /categorias     (ajustá el endpoint según tu backend)
        try {
            return metamapaApi.get()
                    .uri("/categorias")
                    .retrieve()
                    .bodyToFlux(String.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<String> getEtiquetas() {
        try {
            return metamapaApi.get()
                    .uri("/etiquetas")
                    .retrieve()
                    .bodyToFlux(String.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            return List.of();
        }
    }

    // (Tus métodos de patch y delete los dejé igual que antes)
    public boolean patchByHash(String hash, String nombre, String descripcion, List<String> etiquetas) {
        if (hash == null || hash.isBlank()) return false;

        Map<String, Object> body = new HashMap<>();
        if (nombre != null) body.put("nombre", nombre);
        if (descripcion != null) body.put("descripcion", descripcion);
        if (etiquetas != null) body.put("etiquetas", etiquetas);

        if (body.isEmpty()) return true;

        try {
            adminApi.patch()
                    .uri("/hechos/{hash}", hash)
                    .bodyValue(body)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
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
                    .block();
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