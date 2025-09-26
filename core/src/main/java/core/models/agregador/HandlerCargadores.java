package core.models.agregador;

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
import java.util.Collections;
import java.util.List;

public class HandlerCargadores {

    private static volatile HandlerCargadores instance;
    List<HechoAIntegrarDTO> resultado = new ArrayList<>();
    List<HechoAIntegrarDTO> hechosExtraidos = new ArrayList<>();

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
    String path = "/obtenerHechos";
    String dinamico = ConfigLoader.getProperty("CargadorDinamico").concat(path);
    String proxy = ConfigLoader.getProperty("CargadorProxy").concat(path);
    String estatico = ConfigLoader.getProperty("CargadorEstatico").concat(path);
    List<HechoAIntegrarDTO> hechosAIntegrar = new ArrayList<>();

    public List<HechoAIntegrarDTO> extraerHechosAIntegrar() {
        // Lista local, nueva en cada ejecución
        List<HechoAIntegrarDTO> resultado = new ArrayList<>();

        try {
            List<HechoAIntegrarDTO> d = extraerHecho(dinamico);
            if (d != null) resultado.addAll(d);
            System.out.println("[DINAMICO] items: " + (d == null ? 0 : d.size()));
        } catch (Exception e) {
            System.err.println("No se pudo extraer de DINAMICO: " + e.getMessage());
        }

        try {
            List<HechoAIntegrarDTO> p = extraerHecho(proxy);
            if (p != null) resultado.addAll(p);
            System.out.println("[PROXY] items: " + (p == null ? 0 : p.size()));
        } catch (Exception e) {
            System.err.println("No se pudo extraer de PROXY: " + e.getMessage());
        }

        try {
            List<HechoAIntegrarDTO> eList = extraerHecho(estatico);
            if (eList != null) resultado.addAll(eList);
            System.out.println("[ESTATICO] items: " + (eList == null ? 0 : eList.size()));
        } catch (Exception e) {
            System.err.println("No se pudo extraer de ESTATICO: " + e.getMessage());
        }

        System.out.println("[TOTAL en esta llamada] " + resultado.size());
        return resultado; // Nueva lista en cada invocación
    }

    public List<HechoAIntegrarDTO> extraerHecho(String fuente) {
        // Lista LOCAL (no campo compartido)
        List<HechoAIntegrarDTO> hechosExtraidos = new ArrayList<>();

        if (fuente == null || fuente.isBlank()) {
            System.err.println("Fuente vacía o nula");
            return hechosExtraidos; // lista vacía nueva
        }

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

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
            if (status != 200) {
                throw new RuntimeException("Error en la llamada HTTP (" + status + "): " + response.body());
            }

            HechoAIntegrarDTO[] array = objectMapper.readValue(response.body(), HechoAIntegrarDTO[].class);
            Collections.addAll(hechosExtraidos, array);

            // Devolvés SIEMPRE una lista NUEVA, no compartida
            return hechosExtraidos;
        } catch (Exception e) {
            System.out.println("Error al extraer hechos desde " + fuente + ": " + e.getMessage());
            return List.of(); // inmutable y segura
        }
    }
}