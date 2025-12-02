
package core.models.repository;

import core.api.DTO.ColeccionConTodoDTO;
import core.api.DTO.ColeccionDTO;
import core.api.DTO.FuenteDTO;
import core.api.DTO.HechoResumenDTO;
import core.api.DTO.criterio.CriterioDTO;
import core.models.entities.colecciones.Coleccion;
import core.models.entities.colecciones.criterios.Criterio;
import core.models.entities.fuentes.Fuente;
import core.models.entities.hecho.Etiqueta;
import core.models.entities.hecho.Hecho;
import org.hibernate.Hibernate;
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
                    """, Coleccion.class)
                    .setParameter("id", idColeccion)
                    .getResultStream()
                    .findFirst();

            q.ifPresent(c -> {
                for (Hecho h : c.getHechos()) {
                    Hibernate.initialize(h.getEtiquetas());
                }
            });
            return q;
        } finally {
            try {
                em.close();
            } catch (Exception ignore) {
            }
        }
    }

    public Optional<Coleccion> findByIdFetchFuentes(Integer idColeccion) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            var q = em.createQuery("""
                        SELECT DISTINCT c
                        FROM coleccion c
                        LEFT JOIN FETCH c.fuentes f
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


    public Optional<ColeccionConTodoDTO> findColeccionCompletaDTO(Integer idColeccion) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            //Traer colección base
            Coleccion c = em.createQuery("""
            SELECT c
            FROM coleccion c
            WHERE c.id = :id
        """, Coleccion.class)
                    .setParameter("id", idColeccion)
                    .getResultStream().findFirst().orElse(null);

            if (c == null) return Optional.empty();

            //Traer fuentes
            List<Fuente> fuentes = em.createQuery("""
            SELECT f
            FROM coleccion c
            JOIN c.fuentes f
            WHERE c.id = :id
        """, Fuente.class)
                    .setParameter("id", idColeccion)
                    .getResultList();

            //Traer hechos
            List<Hecho> hechos = em.createQuery("""
            SELECT DISTINCT h
            FROM coleccion c
            JOIN c.hechos h
            LEFT JOIN FETCH h.contribuyente
            LEFT JOIN FETCH h.ubicacion
            WHERE c.id = :id
        """, Hecho.class)
                    .setParameter("id", idColeccion)
                    .getResultList();

            //TRAER HECHOS VISIBLES???
            List<Hecho> hechosVisibles = em.createQuery("""
            SELECT h
            FROM coleccion c
            JOIN c.hechosVisibles h
            WHERE c.id = :id
        """, Hecho.class)
                    .setParameter("id", idColeccion)
                    .getResultList();

            //traer criterios
            List<Criterio> criterios = em.createQuery("""
            SELECT cr
            FROM coleccion c
            JOIN c.criterioDePertenencia cr
            WHERE c.id = :id
        """, Criterio.class)
                    .setParameter("id", idColeccion)
                    .getResultList();

            // traer etiquetas
            java.util.Map<String, List<String>> etiquetasPorHash = java.util.Collections.emptyMap();
            if (!hechos.isEmpty()) {
                List<String> hashes = hechos.stream().map(Hecho::getHash).toList();
                List<Object[]> filas = em.createQuery("""
                SELECT h.hash, e.nombre
                FROM hecho h
                JOIN h.etiquetas e
                WHERE h.hash IN :hashes
            """, Object[].class)
                        .setParameter("hashes", hashes)
                        .getResultList();

                etiquetasPorHash = new java.util.HashMap<>();
                for (Object[] row : filas) {
                    String hash = (String) row[0];
                    String tipo  = (String) row[1];
                    etiquetasPorHash.computeIfAbsent(hash, k -> new java.util.ArrayList<>()).add(tipo);
                }
            }

            // Mapear a DTOs
            List<FuenteDTO> fuenteDTOs = fuentes.stream().map(FuenteDTO::from).toList();

            List<HechoResumenDTO> hechoDTOs = new java.util.ArrayList<>(hechos.size());
            for (Hecho h : hechos) {
                List<String> etiquetas = (h.getEtiquetas() != null)
                        ? h.getEtiquetas().stream()
                        .map(Etiqueta::getNombre)
                        .toList()
                        : Collections.emptyList();

                hechoDTOs.add(
                        new HechoResumenDTO(
                                h.getHash(),
                                h.getTitulo(),
                                h.getDescripcion(),
                                (h.getContribuyente()!=null ? h.getContribuyente().getNombreCompleto() : null),
                                h.getFechaSuceso(),
                                h.getHoraSuceso(),
                                null,
                                etiquetas,
                                h.getUbicacion() != null ? h.getUbicacion().getLatitud().toString() : null,
                                h.getUbicacion() != null ? h.getUbicacion().getLongitud().toString() : null,
                                Collections.singletonList(h.getCategoria().toString()),
                                h.getEstado().toString()
                        )
                );
            }

            List<HechoResumenDTO> hechoVisiblesDTOs = hechosVisibles.stream()
                    .map(HechoResumenDTO::from) // o fromCompleto si querés enriquecerlos también
                    .toList();

            List<CriterioDTO> criterioDTOs = criterios.stream()
                    .map(CriterioDTO::from)
                    .toList();

            ColeccionConTodoDTO dto = new ColeccionConTodoDTO(
                    c.getId(),
                    c.getTitulo(),
                    c.getDescripcionColeccion(),
                    fuenteDTOs,
                    hechoDTOs,
                    hechoVisiblesDTOs,
                    criterioDTOs
            );

            return Optional.of(dto);

        } finally {
            try { em.close(); } catch (Exception ignore) {}
        }
    }


    public List<ColeccionDTO> listarColeccionesDTOConCantidadHechos() {
        EntityManager em = DBUtils.getEntityManager();
        try {
            //Traer datos de TODAS las colecciones
            List<Object[]> bases = em.createQuery("""
            SELECT c.id, c.titulo, c.descripcionColeccion, c.identificadorHandle
            FROM coleccion c
            ORDER BY c.id DESC
        """, Object[].class).getResultList();

            //Traer conteo de hechos por colección
            List<Object[]> rows = em.createQuery("""
            SELECT c.id, COUNT(h)
            FROM coleccion c
            LEFT JOIN c.hechos h
            GROUP BY c.id
        """, Object[].class).getResultList();

            Map<Integer, Long> conteos = new HashMap<>();
            for (Object[] r : rows) {
                conteos.put((Integer) r[0], (Long) r[1]);
            }

            //Armar DTO
            List<ColeccionDTO> dtos = new ArrayList<>(bases.size());
            for (Object[] b : bases) {
                Integer id = (Integer) b[0];
                String  titulo = (String) b[1];
                String  descripcion = (String) b[2];
                String  handle = (String) b[3];

                ColeccionDTO dto = new ColeccionDTO();
                dto.setId(id);
                dto.setTitulo(titulo);
                dto.setDescripcionColeccion(descripcion);
                dto.setIdentificadorHandle(handle);
                dto.setCantidadHechos(Math.toIntExact(conteos.getOrDefault(id, 0L)));

                dtos.add(dto);
            }
            return dtos;

        } finally {
            try { em.close(); } catch (Exception ignore) {}
        }
    }

    public List<Hecho> getHechosConUbicacion(Integer idColeccion) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            return em.createQuery(
                            "select h " +
                                    "from coleccion c " +
                                    " join c.hechos h " +
                                    " left join fetch h.ubicacion " +
                                    "where c.id = :id", Hecho.class)
                    .setParameter("id", idColeccion)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    /**
     * Reemplaza completamente las fuentes de una colección por las seleccionadas (solo las tildadas quedan asociadas).
     */
    public void actualizarFuentesDeColeccion(int idColeccion, List<Integer> idsFuentes) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            DBUtils.comenzarTransaccion(em);
            Coleccion coleccion = em.find(Coleccion.class, idColeccion, LockModeType.PESSIMISTIC_WRITE);
            if (coleccion == null) {
                throw new IllegalArgumentException("No existe la Coleccion con id=" + idColeccion);
            }

            // Inicializar la lista si hace falta
            if (coleccion.getFuentes() == null) {
                coleccion.setFuentes(new ArrayList<>());
            }

            // Limpiar fuentes actuales
            coleccion.getFuentes().clear();

            // Agregar nuevas fuentes (solo las seleccionadas)
            if (idsFuentes != null) {
                for (Integer idFuente : idsFuentes) {
                    if (idFuente == null) continue;
                    Fuente ref = em.getReference(Fuente.class, idFuente);
                    coleccion.getFuentes().add(ref);
                }
            }

            DBUtils.commit(em);
        } catch (RuntimeException ex) {
            DBUtils.rollback(em);
            throw ex;
        } finally {
            try { em.close(); } catch (Exception ignore) {}
        }
    }


}
