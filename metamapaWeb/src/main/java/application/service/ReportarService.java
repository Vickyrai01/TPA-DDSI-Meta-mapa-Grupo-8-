package application.service;

import application.dto.HechoDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ReportarService {
    private final WebClient metamapaApi = WebClient.create("http://localhost:8081/core/api");

    public String getCategorias(){
        return metamapaApi.get()
                .uri("/categorias")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public List<HechoDTO> getAll() {
        return metamapaApi.get()
                .uri("/hechos")
                .retrieve()
                .bodyToFlux(HechoDTO.class)
                .collectList()
                .block();
    }

    public ResponseEntity<Void> postearHecho(String hechoJson){
        return metamapaApi.post()
                .uri("/hechos/reportar")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(hechoJson)
                .retrieve()
                .toBodilessEntity()  // o .bodyToMono(Void.class)
                .block();
    }
}