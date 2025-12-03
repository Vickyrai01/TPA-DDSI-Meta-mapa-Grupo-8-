package core.api.handlers.fuentes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.api.DTO.FuenteDTO;
import core.api.DTO.HechoAIntegrarDINAMICO;
import core.api.handlers.colecciones.PatchAgregarFuentesColeccionHandler;
import core.models.agregador.ConfigLoader;
import core.models.entities.fuentes.*;
import core.models.repository.FuentesRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.plaf.PanelUI;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class PostFuenteHandler implements Handler {

    FuentesRepository fuentesRepository = FuentesRepository.getInstance();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String CARGADOR_ESTATICO_BASE_URL = ConfigLoader.getProperty("CargadorEstatico");
    private static final String CARGADOR_DINAMICO_BASE_URL = ConfigLoader.getProperty("CargadorDinamico");

    @Override
    public void handle(@NotNull Context context) throws Exception {
        System.out.println("Body crudo: " + context.body());

        FuenteDTO dto = context.bodyAsClass(FuenteDTO.class);
        System.out.println("DTO.strategyTipoConexion = " + dto.getStrategyTipoConexion());

        StrategyTipoConexion strategyTipoConexion = strategyStringToStrategy(dto.getStrategyTipoConexion());
        System.out.println("Strategy creada = " + strategyTipoConexion
                + " (tipo: " + (strategyTipoConexion != null ? strategyTipoConexion.getClass().getSimpleName() : "null") + ")");

        TipoFuente tipoFuente = obtenerTipoFuente(dto.getStrategyTipoConexion());

        if (strategyTipoConexion == null || tipoFuente == null) {
            context.status(400).result("Tipo de fuente / estrategia no reconocidos");
            return;
        }

        Fuente fuenteCore = new Fuente(
                dto.getNombre(),
                dto.getLink(),
                tipoFuente,
                strategyTipoConexion
        );

        //fuentesRepository.add(fuente);

        // 4) Construir el JSON que se manda al cargador
        //    Reusamos FuenteDTO pero completando tipoFuente y strategyTipoConexion
        FuenteDTO dtoParaCargador = new FuenteDTO();
        dtoParaCargador.setNombre(dto.getNombre());
        dtoParaCargador.setLink(dto.getLink());
        dtoParaCargador.setTipoFuente(tipoFuente.name());                 // "ESTATICA" o "PROXY"
        dtoParaCargador.setStrategyTipoConexion(dto.getStrategyTipoConexion()); // "CSV" o "API REST"

        String jsonBody = mapper.writeValueAsString(dtoParaCargador);

        // 5) Elegir a qué cargador pegarle según la estrategia
        String urlCargador;

        if ("CSV".equalsIgnoreCase(dto.getStrategyTipoConexion())) {
            // fuente estática → cargadorEstatica
            urlCargador = CARGADOR_ESTATICO_BASE_URL + "/agregarFuente";
        } else if ("API REST".equalsIgnoreCase(dto.getStrategyTipoConexion())) {
            // fuente API → cargador dinámico / proxy
            urlCargador = CARGADOR_DINAMICO_BASE_URL + "/agregarFuente";
        } else {
            context.status(400).result("StrategyTipoConexion no soportada: " + dto.getStrategyTipoConexion());
            return;
        }

        // 6) Armar request HTTP hacia el cargador
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlCargador))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // 7) Enviar al cargador y manejar respuesta
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() / 100 != 2) {
            System.out.println("[Core] Error al mandar fuente al cargador: "
                    + response.statusCode() + " body=" + response.body());

            // opcional: podrías deshacer (no guardar la fuente en el core si falló)
            context.status(502).result("Error al registrar fuente en cargador");
            return;
        }

        // Parsear lo que devolvió el cargador (incluye id remoto)
        FuenteDTO respuestaCargador = mapper.readValue(response.body(), FuenteDTO.class);

        context.status(201).json(respuestaCargador);
    }

    public static TipoFuente fromString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return TipoFuente.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public StrategyTipoConexion strategyStringToStrategy(String strategy) {
        if(strategy == null)
        { System.out.println("LLEGA NULL");}
        if ("API REST".equalsIgnoreCase(strategy)) {
            return new StrategyAPIREST();
        }

        if ("CSV".equalsIgnoreCase(strategy)) {
            return new StrategyCSV();
        }

        return null;
    }


    public TipoFuente obtenerTipoFuente(String tipoFuente)
    { if("CSV".equalsIgnoreCase(tipoFuente))
        {TipoFuente idFuente = fromString("ESTATICA");
            return idFuente;}
        if("API REST".equalsIgnoreCase(tipoFuente))
             {TipoFuente idFuente = fromString("PROXY");
                 return idFuente;}
        return null;
    }
}
