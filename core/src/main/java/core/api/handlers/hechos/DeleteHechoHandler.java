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
        String val = ctx.pathParam("val"); // puede ser hash o id
        if (val == null || val.isBlank()) {
            ctx.status(400).result("Identificador requerido");
            return;
        }

        EntityManager em = DBUtils.getEntityManager();
        try {
            DBUtils.comenzarTransaccion(em);

            Hecho hecho = buscarHechoPorHashOId(em, val);
            if (hecho == null) {
                DBUtils.rollback(em);
                ctx.status(404).result("Hecho no encontrado");
                return;
            }

            Hecho managed = em.contains(hecho) ? hecho : em.merge(hecho);
            em.remove(managed);

            DBUtils.commit(em);
            ctx.status(204);
        } catch (Exception ex) {
            DBUtils.rollback(em);
            ctx.status(500).result("Error eliminando hecho");
        } finally {
            try { em.close(); } catch (Exception ignore) {}
        }
    }

    private Hecho buscarHechoPorHashOId(EntityManager em, String val) {
        try {
            return em.createQuery("from hecho h where lower(h.hash)=:hs", Hecho.class)
                    .setParameter("hs", val.trim().toLowerCase())
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException nre) {
            try {
                int id = Integer.parseInt(val);
                return em.find(Hecho.class, id);
            } catch (Exception ignore) {
                return null;
            }
        }
    }
}