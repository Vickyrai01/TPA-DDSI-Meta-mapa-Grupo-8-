package core.models.agregador.cargadores;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import core.api.DTO.HechoAIntegrarDTO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HandlerCargadores {

    private static volatile HandlerCargadores instance;

    private HandlerCargadores() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() para obtener el Singleton");
        }
    }

    public static HandlerCargadores getInstance() {
        if (instance == null) {
            synchronized (HandlerCargadores.class) {
                if (instance == null) {
                    instance = new HandlerCargadores();
                }
            }
        }
        return instance;
    }

    String dinamico = ConfigLoader.getProperty("CargadorDinamico");
    String proxy = ConfigLoader.getProperty("CargadorProxy");
    String estatico = ConfigLoader.getProperty("CargadorEstatico");
    List<HechoAIntegrarDTO> hechosAIntegrar = new ArrayList<>();

    public List<HechoAIntegrarDTO> extraerHechosAIntegrar() {
        List<HechoAIntegrarDTO> resultado = new ArrayList<>();

        try {
            List<HechoAIntegrarDTO> d = extraerHecho(dinamico);
            if (d != null) resultado.addAll(d);
        } catch (Exception e) {
            System.err.println("No se pudo extraer de DINAMICO: " + e.getMessage());
        }

        try {
            List<HechoAIntegrarDTO> p = extraerHecho(proxy);
            if (p != null) resultado.addAll(p);
        } catch (Exception e) {
            System.err.println("No se pudo extraer de PROXY: " + e.getMessage());
        }

        try {
            List<HechoAIntegrarDTO> eList = extraerHecho(estatico);
            if (eList != null) resultado.addAll(eList);
        } catch (Exception e) {
            System.err.println("No se pudo extraer de ESTATICO: " + e.getMessage());
        }

        return resultado;
    }

    public List<HechoAIntegrarDTO> extraerHecho(String fuente) {
        List<HechoAIntegrarDTO> hechosExtraidos = new ArrayList<>();

        if (fuente == null || fuente.isBlank()) {
            System.err.println("Fuente vacía o nula");
            return hechosExtraidos;
        }

        // ObjectMapper configuration
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        // Use Java 21 HttpClient instead of CXF/JAX-RS to avoid javax/jakarta conflicts
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fuente))
                .timeout(Duration.ofSeconds(20))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();
            String responseBody = response.body();

            if (status != 200) {
                throw new RuntimeException("Error en la llamada HTTP (" + status + "): " + responseBody);
            }

            HechoAIntegrarDTO[] array = objectMapper.readValue(responseBody, HechoAIntegrarDTO[].class);
            for (HechoAIntegrarDTO dto : array) {
                hechosExtraidos.add(dto);
            }

            return hechosExtraidos;

        } catch (Exception e) {
            System.out.println("Error al extraer hechos desde " + fuente + ": " + e.getMessage());
            //e.printStackTrace();
            return new ArrayList<>();
        }
    }
}