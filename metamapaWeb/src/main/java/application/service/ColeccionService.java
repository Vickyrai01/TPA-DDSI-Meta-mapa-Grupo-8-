package application.service;

import application.dto.ColeccionDTO;
import application.dto.HechoDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ColeccionService {
    private final WebClient metamapaApi = WebClient.create("http://localhost:8081/core/api");
    private final WebClient metamapaApiADMIN = WebClient.create("http://localhost:8082/core/api");

    // Obtener todas las colecciones del core
    public List<ColeccionDTO> getAll() {
        return metamapaApiADMIN.get()
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
                .filter(h -> {
                    String estado = h.estado();
                    return estado == null || !estado.trim().equals("INACTIVO");
                })
                .collectList()
                .block();
    }

    public List<HechoDTO> getHechosVisibles(Integer id, String modoDeNavegacion) {
        return metamapaApi.get()
                .uri(uri -> uri.path("/colecciones/{id}/{modoDeNavegacion}/hechos")
                        .build(id, modoDeNavegacion))
                .retrieve()
                .bodyToFlux(HechoDTO.class)
                .filter(h -> {
                    String estado = h.estado();
                    return estado == null || !estado.trim().equals("INACTIVO");
                })
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
    public boolean deleteById(Integer id) {
        try {
            var resp = metamapaApiADMIN.delete()
                    .uri(uri -> uri.path("/colecciones/{id}").build(id)) // baseUrl: http://localhost:8082/core/api
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return resp != null && resp.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    //VER
    public boolean patchColeccion(Integer id, String nuevoTitulo, String nuevaDescripcion, Object fuentes, String algoritmoConsenso, String modoDeNavegacion, Object criterios) {
        Map<String, Object> body = new HashMap<>();
        if (nuevoTitulo != null && !nuevoTitulo.isBlank()) {
            body.put("titulo", nuevoTitulo);
        }
        if (nuevaDescripcion != null && !nuevaDescripcion.isBlank()) {
            body.put("descripcionColeccion", nuevaDescripcion);
        }
        if (fuentes != null) {
            body.put("fuentes", fuentes);
        }
        if (algoritmoConsenso != null && !algoritmoConsenso.isBlank()) {
            body.put("algoritmoConsenso", algoritmoConsenso);
        }
        if (modoDeNavegacion != null && !modoDeNavegacion.isBlank()) {
            body.put("modoDeNavegacion", modoDeNavegacion);
        }
        if (criterios != null) {
            body.put("criterioDePertenencia", criterios);
        }
        try {
            var resp = metamapaApiADMIN
                    .patch()
                    .uri("/colecciones/{id}", id)
                    .bodyValue(body)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return resp != null && resp.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }
    public boolean crearColeccion(Map<String, Object> payload) {
        try {
            // Usamos metamapaApiADMIN (puerto 8082)
            var resp = metamapaApiADMIN
                    .post() // Usamos POST
                    .uri("/colecciones") // El endpoint del 'core'
                    .bodyValue(payload) // Enviamos el JSON
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            // El 'core' devuelve 201 (Created) si tiene éxito
            return resp != null && resp.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            // Imprime el error si el 'core' está caído o rechaza la petición
            e.printStackTrace();
            return false;
        }
    }
}

