package core.api.handlers.fuentes;

import core.models.agregador.ConfigLoader;
import core.models.repository.ColeccionesRepository;
import core.models.repository.FuentesRepository;
import core.models.repository.HechosRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DeleteFuenteHandler implements Handler {

    private final HechosRepository hechosRepository = HechosRepository.getInstance();
    private final ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
    private final FuentesRepository fuentesRepository = FuentesRepository.getInstance();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private static final String CARGADOR_ESTATICO_BASE_URL =
            ConfigLoader.getProperty("CargadorEstatico");   // ej: http://localhost:8085/cargadorEstatico
    private static final String CARGADOR_PROXY_BASE_URL =
            ConfigLoader.getProperty("CargadorProxy");      // ej: http://localhost:8084/cargadorProxy

    @Override
    public void handle(@NotNull Context context) throws Exception {
        Integer id = context.pathParamAsClass("id", Integer.class).get();

        boolean eliminadoEnEstatica = eliminarEnCargador(CARGADOR_ESTATICO_BASE_URL + "/eliminar/" + id);
        boolean eliminadoEnProxy   = eliminarEnCargador(CARGADOR_PROXY_BASE_URL   + "/eliminar/" + id);

        if (eliminadoEnEstatica || eliminadoEnProxy) {
            hechosRepository.eliminarHechosPorIdFuente(id);
            coleccionesRepository.eliminarFuenteDeTodasLasColecciones(id);
            fuentesRepository.deleteById(id);
            context.status(200).result("Fuente con ID " + id + " eliminada");
        } else {
            context.status(404).result("La fuente con ID " + id + " no pudo ser eliminada");
        }
    }

    /**
     * Envía un DELETE al cargador. Devuelve true si se eliminó (2xx),
     * false si es 404 o hubo error.
     */
    private boolean eliminarEnCargador(String url) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(java.time.Duration.ofSeconds(10))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());

            int status = resp.statusCode();
            if (status / 100 == 2) {
                return true;          // borró OK
            }
            if (status == 404) {
                return false;         // no la encontró, pero no es grave
            }

            System.out.println("[Core] Error al eliminar fuente en " + url +
                    " status=" + status + " body=" + resp.body());
            return false;
        } catch (Exception e) {
            System.out.println("[Core] Excepción al llamar a " + url);
            e.printStackTrace();
        }
        return false;
    }
}
