package servicioEstadisticas.repository;

import servicioEstadisticas.model.Hecho;
import servicioEstadisticas.model.SolicitudSpam;
import utils.DBUtils;
import javax.persistence.EntityManager;

public class RepositoryServicioEstadisticas {

    private static volatile RepositoryServicioEstadisticas instance;
    public static RepositoryServicioEstadisticas getInstance() {
        if (instance == null) {
            synchronized (RepositoryServicioEstadisticas.class) {
                if (instance == null) {
                    instance = new RepositoryServicioEstadisticas();
                }
            }
        }
        return instance;
    }

    public static String provinciaConMasHechos(){

        EntityManager em = DBUtils.getEntityManager();
        try {
            String jpql = "SELECT h.provincia FROM Hecho h GROUP BY h.provincia ORDER BY COUNT(h) DESC";
            return em.createQuery(jpql, String.class)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception e) {
            return "No se encontraron hechos";
        } finally {
            em.close();
        }
    }

    public static String categoriaMasReportada(){

        EntityManager em = DBUtils.getEntityManager();
        try {
            String jpql = "SELECT h.categoria FROM Hecho h GROUP BY h.categoria ORDER BY COUNT(h) DESC";
            return em.createQuery(jpql, String.class)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception e) {
            return "No se encontraron hechos";
        } finally {
            em.close();
        }
    }

    public static Integer cantidadSolicitudesEliminacion() {
        EntityManager em = DBUtils.getEntityManager();
        try {
            String jpql = "SELECT COUNT(s) FROM SolicitudSpam s WHERE s.fueSpam = true";
            Long cantidad = em.createQuery(jpql, Long.class)
                    .getSingleResult();
            return cantidad.intValue();
        } catch (Exception e) {
            return -1;
        } finally {
            em.close();
        }
    }
    
    public static String horarioxCategoria(String categoria) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            String jpql = "SELECT FUNCTION('HOUR', h.fechaSuceso) as hora, COUNT(h) as cantidad " +
                    "FROM Hecho h " +
                    "WHERE h.categoria = :categoria " +
                    "GROUP BY FUNCTION('HOUR', h.fechaSuceso) " +
                    "ORDER BY COUNT(h) DESC";
            Object[] resultado = (Object[]) em.createQuery(jpql)
                    .setParameter("categoria", categoria)
                    .setMaxResults(1)
                    .getSingleResult();
            return "Hora con más hechos: " + resultado[0] + ":00 (Cantidad: " + resultado[1] + ")";
        } catch (Exception e) {
            return "No se encontraron hechos para esta categoría";
        } finally {
            em.close();
        }
    }


    public static String provicniaConMasHechosEnCategoria(String categoria) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            String jpql = "SELECT h.provincia FROM Hecho h " +
                    "WHERE h.categoria = :categoria " +
                    "GROUP BY h.provincia " +
                    "ORDER BY COUNT(h) DESC";
            return em.createQuery(jpql, String.class)
                    .setParameter("categoria", categoria)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception e) {
            return "No se encontraron hechos para esta categoría";
        } finally {
            em.close();
        }
    }

    public static void addHecho(Hecho hecho) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(hecho);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public static void addSolicitud(SolicitudSpam solicitudSpam) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(solicitudSpam);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }


}
