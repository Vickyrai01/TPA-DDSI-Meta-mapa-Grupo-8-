package core.api.handlers.fuentes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.api.DTO.FuenteDTO;
import core.models.agregador.ConfigLoader;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetFuentesHandler implements Handler {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String CARGADOR_ESTATICO_BASE_URL =
            ConfigLoader.getProperty("CargadorEstatico");
    private static final String CARGADOR_PROXY_BASE_URL =
            ConfigLoader.getProperty("CargadorProxy");

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        List<FuenteDTO> todas = new ArrayList<>();

        // Fuentes CSV (estáticas)
        todas.addAll(
                fetchFuentesRemotas(
                        CARGADOR_ESTATICO_BASE_URL + "/obtenerFuentes",
                        "ESTATICA",
                        "CSV"
                )
        );

        // Fuentes API REST (proxy/dinámicas)
        todas.addAll(
                fetchFuentesRemotas(
                        CARGADOR_PROXY_BASE_URL + "/obtenerFuentes",
                        "PROXY",
                        "API REST"
                )
        );

        ctx.json(todas);
    }

    // Lee la lista de fuentes de un cargador y las adapta a FuenteDTO del core
    private List<FuenteDTO> fetchFuentesRemotas(String url,
                                                String tipoFuente,
                                                String strategyTipoConexion) {
        List<FuenteDTO> resultado = new ArrayList<>();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Si el cargador devuelve 204 o algo raro, devolvemos lista vacía
            if (response.statusCode() / 100 != 2 ||
                    response.body() == null ||
                    response.body().isBlank()) {
                System.out.println("[Core] No se pudieron obtener fuentes desde: " + url
                        + " status=" + response.statusCode());
                return resultado;
            }

            // Leemos como lista de Map para no depender de que el JSON tenga EXACTAMENTE
            // la misma forma que nuestro FuenteDTO.
            List<Map<String, Object>> rawList = mapper.readValue(
                    response.body(),
                    new TypeReference<List<Map<String, Object>>>() {}
            );

            for (Map<String, Object> raw : rawList) {
                Integer id = raw.get("id") != null ? ((Number) raw.get("id")).intValue() : null;
                String nombre = raw.get("nombre") != null ? raw.get("nombre").toString() : "";
                String link = raw.get("link") != null ? raw.get("link").toString() : null;

                FuenteDTO dto = new FuenteDTO(
                        id,
                        nombre,
                        link,
                        tipoFuente,            // "ESTATICA" o "PROXY"
                        strategyTipoConexion  // "CSV" o "API REST"
                );
                resultado.add(dto);
            }

        } catch (Exception e) {
            System.out.println("[Core] Error llamando a " + url);
            e.printStackTrace();
        }

        return resultado;
    }
}
