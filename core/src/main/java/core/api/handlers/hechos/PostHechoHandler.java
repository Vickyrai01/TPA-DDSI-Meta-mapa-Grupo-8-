package core.api.handlers.hechos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.api.handlers.colecciones.PatchAgregarFuentesColeccionHandler;
import core.models.agregador.cargadores.ConfigLoader;
import core.models.entities.fuentes.Fuente;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.api.DTO.HechoAIntegrarDTO;
import core.models.repository.DinamicaRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class PostHechoHandler implements Handler {
    private final DinamicaRepository repoDinamicos = DinamicaRepository.getInstance();
    private static final Logger log = LoggerFactory.getLogger(PatchAgregarFuentesColeccionHandler.class);

    @Override
    public void handle(@NotNull Context context) throws Exception {
        HechoAIntegrarDTO dto = context.bodyAsClass(HechoAIntegrarDTO.class);
        System.out.println("Creando hecho: " + context.body());


        HechoAIntegrarDTO hechoDTO = new HechoAIntegrarDTO(
                dto.getTitulo(),
                dto.getDescripcion(),
                dto.getCategoria(),
                dto.getLatitud(),
                dto.getLongitud(),
                dto.getFechaSuceso()
        );

        validarNuevoHecho(hechoDTO);

        repoDinamicos.add(hechoDTO);
        enviarHechoAlCargador(hechoDTO);
        context.status(201);
    }

    private void validarNuevoHecho(HechoAIntegrarDTO hecho) {
        if (hecho.getTitulo() == null) {
            throw new IllegalArgumentException("El nombre es obligatorio, elegí otro");
        }
    }

    private void enviarHechoAlCargador(HechoAIntegrarDTO hecho) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonHecho = this.hechoAJson(hecho);
        log.info("Hecho a enviar al cargador dinamico: " + jsonHecho);

        String url = ConfigLoader.getProperty("CargadorDinamico");
        url = url.concat("/agregarHecho");

        log.info("URL: " + url);

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(20))
                .header("Content-Type", "application/json; charset=utf-8"
                )
                .POST(HttpRequest.BodyPublishers.ofString(jsonHecho))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();
            String responseBody = response.body();

            if (status != 201) {
                throw new RuntimeException("Error en la llamada HTTP (" + status + "): " + responseBody);
            }
            log.info("Fuente enviada al cargador: " + hecho.getTitulo());
        } catch (Exception e) {
            log.info("Error al enviar fuente " + hecho + ": " + e.getMessage());
        }
    }

    private String hechoAJson(HechoAIntegrarDTO hechoAIntegrarDTO){
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


    public String fuenteAJson(Fuente fuente) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("nombre", fuente.getNombre());
        jsonMap.put("link", fuente.getLink());
        jsonMap.put("tipoFuente", fuente.getStrategyTipoConexion().devolverTipoDeConexion()); // si quieres el string: this.tipoFuente.toString()
        try {
            return mapper.writeValueAsString(jsonMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }
}
