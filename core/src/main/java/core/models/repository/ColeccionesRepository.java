package core.models.repository;

import core.models.entities.colecciones.Coleccion;
import core.models.entities.colecciones.criterios.Criterio;
import core.models.entities.hecho.Hecho;
import utils.DBUtils;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ColeccionesRepository extends JpaRepositoryBase<Coleccion, Integer> {

    private static volatile ColeccionesRepository instance;

    private ColeccionesRepository() {
        super(Coleccion.class, DBUtils::getEntityManager, Coleccion::getId);
    }

    public static ColeccionesRepository getInstance() {
        if (instance == null) {
            synchronized (ColeccionesRepository.class) {
                if (instance == null) instance = new ColeccionesRepository();
            }
        }
        return instance;
    }

    public Coleccion getColeccion(Integer idColeccion){
        return findById(idColeccion);
    }

    public List<Criterio> getCriterios(Integer idColeccion){
        Coleccion coleccion = findById(idColeccion);
        /*
         EntityManager em = emSupplier.get();
        try {
            String ql = "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e";
            return em.createQuery(ql, Long.class).getSingleResult();
        } finally {
            em.close();
        }
         */

        //SELECT criterios FROM coleccion c WHERE  idColeccion = c.id_coleccion

        return coleccion.getCriterioDePertenencia();
    }

    public void agregarHechosAColeccion(int idColeccion, List<Integer> idsHechos) {
        if (idsHechos == null || idsHechos.isEmpty()) return;

        EntityManager em = DBUtils.getEntityManager();
        try {
            DBUtils.comenzarTransaccion(em);

            // 1) Traer Coleccion en modo escritura para evitar carreras concurrentes
            Coleccion coleccion = em.find(Coleccion.class, idColeccion, LockModeType.PESSIMISTIC_WRITE);
            if (coleccion == null) {
                throw new IllegalArgumentException("No existe la Coleccion con id=" + idColeccion);
            }

            // 2) Inicializar la lista si hace falta
            if (coleccion.getHechos() == null) {
                coleccion.setHechos(new ArrayList<>());
            }

            // 3) Obtener ids ya vinculados para evitar duplicados en la join table
            //    (más robusto que confiar en contains() con proxies)
            Set<Integer> existentes = new HashSet<>(
                    em.createQuery(
                                    "select h.id from coleccion c join c.hechos h where c.id = :idCol",
                                    Integer.class
                            ).setParameter("idCol", idColeccion)
                            .getResultList()
            );

            // 4) Agregar sólo los que no están
            for (Integer idHecho : idsHechos) {
                if (idHecho == null) continue;
                if (existentes.contains(idHecho)) continue; // ya está linkeado

                Hecho ref = em.getReference(Hecho.class, idHecho); // requiere que el Hecho exista/esté commiteado
                coleccion.getHechos().add(ref);
                existentes.add(idHecho);
            }

            // 5) Commit: acá JPA inserta en coleccion_hecho (id_coleccion, id_hecho)
            DBUtils.commit(em);
        } catch (RuntimeException ex) {
            DBUtils.rollback(em);
            throw ex;
        } finally {
            try { em.close(); } catch (Exception ignore) {}
        }
    }
}
