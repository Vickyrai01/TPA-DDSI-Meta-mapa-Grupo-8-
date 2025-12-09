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
    private final WebClient metamapaApiADMIN = WebClient.create("http://localhost:8082/core/api");

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

    public ResponseEntity<Void> postearHecho(String hechoJson, boolean urgente){
        return metamapaApi.post()
                .uri("/hechos/reportar")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Urgente", String.valueOf(urgente)) // flag
                .bodyValue(hechoJson)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public ResponseEntity<Void> ejecutarAgregacion(){
        return metamapaApiADMIN.post()
                .uri("/ejecutarServicio")
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}