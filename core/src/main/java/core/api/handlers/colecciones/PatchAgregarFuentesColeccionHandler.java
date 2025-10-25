package core.api.handlers.colecciones;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.api.DTO.ActualizarFuentesColeccionDTO;
import core.models.agregador.ConfigLoader;
import core.models.entities.fuentes.TipoFuente;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.colecciones.Coleccion;
import core.models.entities.fuentes.Fuente;
import core.models.repository.ColeccionesRepository;
import core.models.repository.FuentesRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatchAgregarFuentesColeccionHandler implements Handler
{
   private final ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
    private final FuentesRepository fuentesRepository = FuentesRepository.getInstance();
    private static final Logger log = LoggerFactory.getLogger(PatchAgregarFuentesColeccionHandler.class);
    @Override
    public void handle(@NotNull Context context) throws Exception {
        int idColeccion = Integer.parseInt(context.pathParam("id"));
        ActualizarFuentesColeccionDTO dto = context.bodyAsClass(ActualizarFuentesColeccionDTO.class);

        Coleccion coleccion = coleccionesRepository.getColeccion(idColeccion);
        if (coleccion == null) {
            context.status(404).result("Colección no encontrada");
            return;
        }

        List<Fuente> fuentesActuales = new ArrayList<>(coleccion.getFuentes());

        for (Integer idFuente : dto.fuentes) {
            Fuente fuente = fuentesRepository.getFuente(idFuente);
            if (fuente != null) {
                if (!fuentesActuales.contains(fuente)) {
                    coleccion.agregarFuente(fuente);
                    coleccionesRepository.update(coleccion);
                    enviarFuenteAlCargador(fuente);
                }
            } else {
                context.status(404).result("Fuente con ID " + idFuente + " no encontrada");
                return;
            }
        }

        context.status(200).result("Fuentes agregadas correctamente");
    }

    private void enviarFuenteAlCargador(Fuente fuente) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        //String jsonFuente = objectMapper.writeValueAsString(fuente);

        String jsonFuente = this.fuenteAJson(fuente);
        log.info("Fuente a enviar al cargador: " + jsonFuente);

        String url;
        if(fuente.getTipoFuente().equals(TipoFuente.PROXY)){
            url = ConfigLoader.getProperty("CargadorProxy");
        }
        else{
            url = ConfigLoader.getProperty("CargadorEstatico");
        }
        url = url.concat("/agregarFuente");

        log.info("URL: " + url);

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(20))
                .header("Content-Type", "application/json; charset=utf-8"
                )
                .POST(HttpRequest.BodyPublishers.ofString(jsonFuente))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();
            String responseBody = response.body();

            if (status != 201) {
                throw new RuntimeException("Error en la llamada HTTP (" + status + "): " + responseBody);
            }
            log.info("Fuente enviada al cargador: " + fuente.getNombre());
        } catch (Exception e) {
            log.info("Error al enviar fuente " + fuente + ": " + e.getMessage());
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

