package servicioEstadisticas.repository;

import servicioEstadisticas.model.Hecho;
import utils.DBUtils;

import javax.persistence.EntityManager;

public class RepositoryServicioEstadisticas {

    public static String provinciaConMasHechos(){

        return "ups";
    }

    public static String categoriaMasReportada(){

        return "V";
    }


    public static Integer cantidadSolicitudesEliminacion(){
        return 1;
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


}
