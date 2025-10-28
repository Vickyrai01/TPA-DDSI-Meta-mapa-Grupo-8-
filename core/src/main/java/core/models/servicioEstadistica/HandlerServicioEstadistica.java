package core.models.servicioEstadistica;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.models.agregador.HechoAIntegrarDTO;
import org.apache.cxf.jaxrs.client.WebClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerServicioEstadistica {
    HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private final ObjectMapper mapper = new ObjectMapper();
    String path = "http://localhost:8090/servicioEstadisticas";

    public void enviarHechoAgregado(DTOHechoAgregado dto) throws IOException, InterruptedException {
        String pathPost = path.concat("/hecho");
        String json = mapper.writeValueAsString(dto);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(pathPost))
                .timeout(Duration.ofSeconds(20))
                .header("Content-Type", "application/json; charset=utf-8"
                )
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int status = response.statusCode();
        String responseBody = response.body();

        if (status != 201) {
            throw new RuntimeException("Error en la llamada HTTP (" + status + "): " + responseBody);
        }
    }

    public void enviarSolicitudEliminacion(DTOSolicitudEliminacion dto) throws IOException, InterruptedException {
        String pathPost = path.concat("/solicitudSpam");
        String json = mapper.writeValueAsString(dto);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(pathPost))
                .timeout(Duration.ofSeconds(20))
                .header("Content-Type", "application/json; charset=utf-8"
                )
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int status = response.statusCode();
        String responseBody = response.body();

        if (status != 201) {
            throw new RuntimeException("Error en la llamada HTTP (" + status + "): " + responseBody);
        }
    }

    public void enviarHechosAgregados(List<DTOHechoAgregado> dto){

    }

    public void enviarSolicitudesEliminacion(List<DTOSolicitudEliminacion> dto){

    }
    /*
    private String hechoAJson(DTOHechoAgregado dto){
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("hash", hechoAIntegrarDTO.getHash());
        jsonMap.put("titulo", hechoAIntegrarDTO.getTitulo());
        jsonMap.put("descripcion", hechoAIntegrarDTO.getDescripcion());
        jsonMap.put("categoria", hechoAIntegrarDTO.getCategoria());
        jsonMap.put("latitud", hechoAIntegrarDTO.getLatitud());
        jsonMap.put("longitud", hechoAIntegrarDTO.getLongitud());
        jsonMap.put("fechaSuceso", hechoAIntegrarDTO.getFechaSuceso());
        try {
            return mapper.writeValueAsString(jsonMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }
     */
    //CONEXIÒN
    //METODO POST
    //HABRÍA QUE VER QUE MANDE UNA BATCH
}
