package core.models.agregador;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import core.observabilidad.RegistroMetricas;
import org.slf4j.MDC;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
            List<HechoAIntegrarDTO> d = extraerHechoConMetricas(dinamico, TipoCargador.DINAMICO);
            if (d != null) resultado.addAll(d);
            System.out.println("[DINAMICO] items: " + (d == null ? 0 : d.size()));
        } catch (Exception e) {
            System.err.println("No se pudo extraer de DINAMICO: " + e.getMessage());
        }

        try {
            List<HechoAIntegrarDTO> p = extraerHechoConMetricas(proxy, TipoCargador.PROXY);
            if (p != null) resultado.addAll(p);
            System.out.println("[PROXY] items: " + (p == null ? 0 : p.size()));
        } catch (Exception e) {
            System.err.println("No se pudo extraer de PROXY: " + e.getMessage());
        }

        try {
            List<HechoAIntegrarDTO> eList = extraerHechoConMetricas(estatico, TipoCargador.ESTATICO);
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

        // 1) correlationId del MDC (lo setea el before de Javalin)
        String correlationId = MDC.get("correlationId");
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
            MDC.put("correlationId", correlationId);
        }


        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fuente))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .header("X-Correlation-Id", correlationId)
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

    // ==== NUEVO: enum interno para identificar cargador ====
    private enum TipoCargador { DINAMICO, PROXY, ESTATICO }

    private void registrarRequest(TipoCargador tipo) {
        switch (tipo) {
            case DINAMICO -> RegistroMetricas.incReqDinamico();
            case PROXY    -> RegistroMetricas.incReqProxy();
            case ESTATICO -> RegistroMetricas.incReqEstatico();
        }
    }

    private void registrarError(TipoCargador tipo) {
        switch (tipo) {
            case DINAMICO -> RegistroMetricas.incErrDinamico();
            case PROXY    -> RegistroMetricas.incErrProxy();
            case ESTATICO -> RegistroMetricas.incErrEstatico();
        }
    }

    private void registrarTiempo(TipoCargador tipo, long ms) {
        switch (tipo) {
            case DINAMICO -> RegistroMetricas.addTimeDinamico(ms);
            case PROXY    -> RegistroMetricas.addTimeProxy(ms);
            case ESTATICO -> RegistroMetricas.addTimeEstatico(ms);
        }
    }

    // ==== NUEVO: wrapper con métricas por cargador ====
    private List<HechoAIntegrarDTO> extraerHechoConMetricas(String fuente, TipoCargador tipo) {
        registrarRequest(tipo);
        long start = System.nanoTime();
        try {
            List<HechoAIntegrarDTO> res = extraerHecho(fuente);
            long durMs = (System.nanoTime() - start) / 1_000_000;
            registrarTiempo(tipo, durMs);
            return res;
        } catch (Exception e) {
            long durMs = (System.nanoTime() - start) / 1_000_000;
            registrarTiempo(tipo, durMs);
            registrarError(tipo);
            throw e;
        }
    }
}
