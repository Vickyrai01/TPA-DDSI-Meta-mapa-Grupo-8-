package core.models.repository;

import core.models.entities.colecciones.Coleccion;
import core.models.entities.colecciones.criterios.Criterio;
import core.models.entities.hecho.Hecho;
import utils.DBUtils;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.*;

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

    public Coleccion getColeccion(Integer idColeccion) {
        return findById(idColeccion);
    }

    public List<Criterio> getCriterios(Integer idColeccion) {
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

            //Traer Coleccion
            Coleccion coleccion = em.find(Coleccion.class, idColeccion, LockModeType.PESSIMISTIC_WRITE);
            if (coleccion == null) {
                throw new IllegalArgumentException("No existe la Coleccion con id=" + idColeccion);
            }

            //Inicializar la lista si hace falta
            if (coleccion.getHechos() == null) {
                coleccion.setHechos(new ArrayList<>());
            }

            //Obtener ids ya vinculados para evitar duplicados en la join table
            Set<Integer> existentes = new HashSet<>(
                    em.createQuery(
                                    "select h.id from coleccion c join c.hechos h where c.id = :idCol",
                                    Integer.class
                            ).setParameter("idCol", idColeccion)
                            .getResultList()
            );

            //Agregar sólo los que no están
            for (Integer idHecho : idsHechos) {
                if (idHecho == null) continue;
                if (existentes.contains(idHecho)) continue; // ya está linkeado

                Hecho ref = em.getReference(Hecho.class, idHecho); // requiere que el Hecho exista/esté commiteado
                coleccion.getHechos().add(ref);
                existentes.add(idHecho);
            }

            //Commit: inserta en coleccion_hecho (id_coleccion, id_hecho)
            DBUtils.commit(em);
        } catch (RuntimeException ex) {
            DBUtils.rollback(em);
            throw ex;
        } finally {
            try {
                em.close();
            } catch (Exception ignore) {
            }
        }
    }

    //VER SI SE TRAE LOS VISIBLES O QUE CARAJEANOS!!! (CONSULTAR EQUIPO DINAMITA)
    //EN QUE QUEDO LO DE LA TABLA ESA DE VISIBLES O NO. PORQUE SI NO HAY QUE CAMBIAR.
    public Optional<Coleccion> findByIdFetchHechosYContribuyente(Integer idColeccion) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            var q = em.createQuery("""
                        SELECT DISTINCT c
                        FROM coleccion c
                        LEFT JOIN FETCH c.hechos h
                        LEFT JOIN FETCH h.contribuyente
                        WHERE c.id = :id
                    """, Coleccion.class);
            q.setParameter("id", idColeccion);
            return q.getResultStream().findFirst();
        } finally {
            try {
                em.close();
            } catch (Exception ignore) {
            }
        }

    }
}
