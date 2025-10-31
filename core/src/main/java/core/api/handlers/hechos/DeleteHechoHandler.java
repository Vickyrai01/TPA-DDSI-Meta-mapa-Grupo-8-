package core.api.handlers.hechos;

import core.models.entities.hecho.Hecho;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import utils.DBUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

public class DeleteHechoHandler implements Handler {
    @Override
    public void handle(Context ctx) {
        String hash = ctx.pathParam("hash"); // coincide con {hash} en el config
        if (hash == null || hash.isBlank()) {
            ctx.status(400).result("Hash requerido");
            return;
        }

        EntityManager em = DBUtils.getEntityManager();
        try {
            DBUtils.comenzarTransaccion(em);

            Hecho hecho = buscarPorHash(em, hash);
            if (hecho == null) {
                DBUtils.rollback(em);
                ctx.status(404).result("Hecho no encontrado");
                return;
            }

            Integer id = hecho.getId();

            // Limpiar relaciones para evitar violaciones de FK
            // Ajustá nombres de tablas/columnas si difieren en tu schema
            try { em.createNativeQuery("DELETE FROM coleccion_hecho WHERE id_hecho = :id")
                    .setParameter("id", id).executeUpdate(); } catch (Exception ignore) {}
            try { em.createNativeQuery("DELETE FROM hecho_etiqueta WHERE id_hecho = :id")
                    .setParameter("id", id).executeUpdate(); } catch (Exception ignore) {}
            try { em.createNativeQuery("DELETE FROM hecho_multimedia WHERE hecho_id = :id")
                    .setParameter("id", id).executeUpdate(); } catch (Exception ignore) {}
            try { em.createNativeQuery("DELETE FROM hechos_visibles WHERE id_hecho = :id")
                    .setParameter("id", id).executeUpdate(); } catch (Exception ignore) {}
            // NUEVO: limpiar solicitudes de eliminación que referencian al hecho
            try { em.createNativeQuery("DELETE FROM solicitud_de_eliminacion WHERE hecho_id_hecho = :id")
                    .setParameter("id", id).executeUpdate(); } catch (Exception ignore) {}

            Hecho managed = em.contains(hecho) ? hecho : em.merge(hecho);
            em.remove(managed);

            DBUtils.commit(em);
            ctx.status(204);
        } catch (Exception ex) {
            DBUtils.rollback(em);
            ex.printStackTrace();
            ctx.status(500).result("Error eliminando hecho: " + ex.getMessage());
        } finally {
            try { em.close(); } catch (Exception ignore) {}
        }
    }

    private Hecho buscarPorHash(EntityManager em, String hash) {
        try {
            return em.createQuery("from hecho h where lower(h.hash)=:hs", Hecho.class)
                    .setParameter("hs", hash.trim().toLowerCase())
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}