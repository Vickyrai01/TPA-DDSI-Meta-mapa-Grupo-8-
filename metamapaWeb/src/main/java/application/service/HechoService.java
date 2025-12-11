package application.service;

import application.dto.HechoDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final WebClient metamapaApiADMIN;

    public HechoService(RutasProperties props) {
        this.metamapaApiADMIN = WebClient.create(props.getAdminBaseUrl());
    }

    public List<HechoDTO> getAll() {
        return metamapaApiADMIN.get()
                .uri("/hechos")
                .exchangeToFlux(response -> {
                    if (response.statusCode().is2xxSuccessful() && response.headers().contentType().isPresent() &&
                            response.headers().contentType().get().toString().contains("json")) {
                        return response.bodyToFlux(HechoDTO.class);
                    } else {
                        return response.bodyToFlux(HechoDTO.class);
                    }
                })
                .collectList()
                .block();
    }

    //PARA EL MAPITA DE MIS HECHOS
    public List<HechoDTO> getByContribuyente(String email) {
        if (email == null || email.isBlank()) return List.of();
        try {
            return metamapaApiADMIN.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/hechos")
                            .queryParam("contribuyente", email)
                            .build())
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
        } catch (Exception e) {
            System.err.println("Error en getByContribuyente: " + e.getMessage());
        } catch (WebClientResponseException e) {
            System.err.println("API hechos coleccion error " + e.getStatusCode() + ": " + e.getResponseBodyAsString());
            return List.of();
        } catch (Exception e) {
            System.err.println("API hechos coleccion error: " + e.getMessage());
            return List.of();
        }
    }

    public List<String> getCategorias() {
        try {
            // Acá tu service obtiene el JSON:
            String categoriasJson = metamapaApi.get()
                    .uri("/categorias")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Parsearlo a List<String>
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(
                    categoriasJson,
                    new TypeReference<List<String>>() {}
            );
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<String> getEtiquetas() {
        try {
            String etiquetasJson = metamapaApi.get()
                    .uri("/etiquetas")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(etiquetasJson, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // EDITAR (admin 8082) — PATCH /core/api/hechos/{hash}
    public boolean patchByHash(String hash, String nombre, String descripcion, List<String> etiquetas, String latitud, String longitud, String fechaSuceso, String categoria) {
        if (hash == null || hash.isBlank()) return false;

        Map<String, Object> body = new HashMap<>();
        if (nombre != null) body.put("nombre", nombre);
        if (descripcion != null) body.put("descripcion", descripcion);
        if (etiquetas != null) body.put("etiquetas", etiquetas);
        if (latitud != null) body.put("latitud", latitud);
        if (longitud != null) body.put("longitud", longitud);
        if (fechaSuceso != null) body.put("fecha_suceso", fechaSuceso);
        if (categoria != null) body.put("categoria", categoria);

        if (body.isEmpty()) return true;

        try {
            metamapaApiADMIN.patch()
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
            metamapaApiADMIN.delete()
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