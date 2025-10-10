package servicioEstadisticas.repository;

import servicioEstadisticas.model.Hecho;
import servicioEstadisticas.model.SolicitudSpam;
import utils.DBUtils;
import javax.persistence.EntityManager;

public class RepositoryServicioEstadisticas {
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




    public static String horarioxCategoria(String categoria){
        return "b";
    }


    public static String provicniaConMasHechosEnCategoria(String categoria){
        return "a";
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
