package core.api.handlers.hechos;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.models.entities.hecho.Hecho;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import utils.DBUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.Map;

public class PatchHechoHandler implements Handler {

    private static final ObjectMapper RELAXED_JSON = new ObjectMapper()
            .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
            .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

    @Override
    public void handle(Context ctx) {
        String hash = ctx.pathParam("hash"); // Importante: la ruta debe ser /core/api/hechos/{hash}
        if (hash == null || hash.isBlank()) {
            ctx.status(400).result("Hash requerido");
            return;
        }

        String raw = ctx.body();
        Map<String, Object> body;
        try {
            body = (raw == null || raw.isBlank())
                    ? Collections.emptyMap()
                    : RELAXED_JSON.readValue(raw, new TypeReference<Map<String, Object>>() {});
        } catch (Exception ex) {
            // Si el body no es JSON parseable, devolvemos 400 con detalle
            ctx.status(400).result("Body inválido. Enviar JSON con nombre/descripcion.");
            return;
        }

        String nombre = body.get("nombre") != null ? String.valueOf(body.get("nombre")) : null;
        String descripcion = body.get("descripcion") != null ? String.valueOf(body.get("descripcion")) : null;

        // Si no hay cambios, OK
        if ((nombre == null || nombre.isBlank()) && (descripcion == null || descripcion.isBlank())) {
            ctx.status(204);
            return;
        }

        EntityManager em = DBUtils.getEntityManager();
        try {
            DBUtils.comenzarTransaccion(em);

            Hecho hecho;
            try {
                hecho = em.createQuery("from hecho h where lower(h.hash)=:hs", Hecho.class)
                        .setParameter("hs", hash.trim().toLowerCase())
                        .setMaxResults(1)
                        .getSingleResult();
            } catch (NoResultException nre) {
                DBUtils.rollback(em);
                ctx.status(404).result("Hecho no encontrado");
                return;
            }

            if (nombre != null && !nombre.isBlank()) hecho.setTitulo(nombre);
            if (descripcion != null && !descripcion.isBlank()) hecho.setDescripcion(descripcion);

            em.merge(hecho);
            DBUtils.commit(em);
            ctx.status(204);
        } catch (Exception ex) {
            DBUtils.rollback(em);
            ex.printStackTrace();
            ctx.status(500).result("Error actualizando hecho: " + ex.getMessage());
        } finally {
            try { em.close(); } catch (Exception ignore) {}
        }
    }
}