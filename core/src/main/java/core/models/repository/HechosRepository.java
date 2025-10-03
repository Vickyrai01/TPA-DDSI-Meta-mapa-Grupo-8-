package core.models.repository;

import core.models.entities.fuentes.Fuente;
import core.models.entities.hecho.Hecho;
import utils.DBUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class HechosRepository extends JpaRepositoryBase<Hecho, Integer> {

    private static volatile HechosRepository instance;

    private HechosRepository() {
        super(Hecho.class, DBUtils::getEntityManager, Hecho::getId);
    }

    public static HechosRepository getInstance() {
        if (instance == null) { // verifica si hay instancia
            synchronized (HechosRepository.class) { // Bloqueo para evitar condicion de carrera
                if (instance == null) { // verifica nuevamente si existe instancia
                    instance = new HechosRepository();
                }
            }
        }
        return instance;
    }

    public boolean esHechoDuplicado(Hecho hecho) {
        if (hecho == null || hecho.getTitulo() == null) return false;

        EntityManager em = DBUtils.getEntityManager();
        try {
            Long cantidad = em.createQuery(
                            "SELECT COUNT(h) " +
                                    "FROM hecho h " +
                                    "WHERE LOWER(TRIM(h.titulo)) = LOWER(:titulo)", Long.class)
                    .setParameter("titulo", hecho.getTitulo().trim())
                    .getSingleResult();

            return cantidad > 0;
        } finally {
            em.close();
        }
    }

    public boolean existeElHecho(String hash) {
        if (hash == null || hash.isBlank()) return false;

        EntityManager em = DBUtils.getEntityManager();
        try {
            Long cantidad = em.createQuery(
                            "SELECT COUNT(h) " +
                                    "FROM hecho h " +
                                    "WHERE LOWER(h.hash) = LOWER(:hash)", Long.class)
                    .setParameter("hash", hash.trim())
                    .getSingleResult();

            return cantidad > 0;
        } finally {
            em.close();
        }
    }
}
