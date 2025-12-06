
package core.models.repository;

import core.api.DTO.ColeccionConTodoDTO;
import core.api.DTO.ColeccionDTO;
import core.api.DTO.FuenteDTO;
import core.api.DTO.HechoResumenDTO;
import core.api.DTO.criterio.CriterioDTO;
import core.models.entities.colecciones.*;
import core.models.entities.colecciones.criterios.Criterio;
import core.models.entities.fuentes.Fuente;
import core.models.entities.hecho.Etiqueta;
import core.models.entities.hecho.Hecho;
import org.hibernate.Hibernate;
import utils.DBUtils;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
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

    public Optional<Coleccion> findByIdFetchHechosVisiblesYContribuyente(Integer idColeccion) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            var q = em.createQuery("""
                    SELECT DISTINCT c
                    FROM coleccion c
                    LEFT JOIN FETCH c.hechosVisibles hv
                    LEFT JOIN FETCH hv.contribuyente
                    WHERE c.id = :id
                """, Coleccion.class)
                    .setParameter("id", idColeccion)
                    .getResultStream()
                    .findFirst();

            q.ifPresent(c -> {
                // Inicializo etiquetas de los hechos visibles
                for (Hecho hv : c.getHechosVisibles()) {
                    Hibernate.initialize(hv.getEtiquetas());
                }

                // Inicializo algoritmoConsenso si existe
                if (c.getAlgoritmoConsenso() != null) {
                    c.getAlgoritmoConsenso().toString(); // con tocarlo basta
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
                    String tipo = (String) row[1];
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
                                (h.getContribuyente() != null ? h.getContribuyente().getNombreCompleto() : null),
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

            String modoNavStr = c.getModoDeNavegacion() != null
                    ? c.getModoDeNavegacion().name()
                    : null;

            String algoritmoStr = c.getAlgoritmoConsenso() != null
                    ? c.getAlgoritmoConsenso().devolverTipoDeConsenso()
                    : null;


            ColeccionConTodoDTO dto = new ColeccionConTodoDTO(
                    c.getId(),
                    c.getTitulo(),
                    c.getDescripcionColeccion(),
                    fuenteDTOs,
                    hechoDTOs,
                    hechoVisiblesDTOs,
                    criterioDTOs,
                    modoNavStr,
                    algoritmoStr
            );

            return Optional.of(dto);

        } finally {
            try {
                em.close();
            } catch (Exception ignore) {
            }
        }
    }


    public List<ColeccionDTO> listarColeccionesDTOConCantidadHechos() {
        EntityManager em = DBUtils.getEntityManager();
        try {
            // BASE
            List<Object[]> bases = em.createQuery("""
            SELECT c.id,
                   c.titulo,
                   c.descripcionColeccion,
                   c.identificadorHandle,
                   c.modoDeNavegacion,
                   c.algoritmoConsenso
            FROM coleccion c
            ORDER BY c.id DESC
        """, Object[].class).getResultList();

            // CANTIDAD DE HECHOS
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

            // CANTIDAD DE HECHOS VISIBLES
            List<Object[]> rowsVisibles = em.createQuery("""
                SELECT c.id, COUNT(hv)
                FROM coleccion c
                LEFT JOIN c.hechosVisibles hv
                GROUP BY c.id
            """, Object[].class).getResultList();

            Map<Integer, Long> conteosVisibles = new HashMap<>();
            for (Object[] r : rowsVisibles) {
                conteosVisibles.put((Integer) r[0], (Long) r[1]);
            }
            // FUENTES DE CADA COLECCIÓN → List<Integer> con IDs
            List<Object[]> rowsFuentes = em.createQuery("""
                SELECT c.id, f.id
                FROM coleccion c
                JOIN c.fuentes f
                ORDER BY c.id
            """, Object[].class).getResultList();

            Map<Integer, List<Integer>> fuentesPorColeccion = new HashMap<>();

            for (Object[] r : rowsFuentes) {
                Integer idColeccion = (Integer) r[0];
                Integer idFuente = (Integer) r[1];

                fuentesPorColeccion
                        .computeIfAbsent(idColeccion, k -> new ArrayList<>())
                        .add(idFuente);
            }

            // CRITERIOS DE CADA COLECCIÓN
            List<Object[]> rowsCriterios = em.createQuery("""
                SELECT c.id, crit
                FROM coleccion c
                JOIN c.criterioDePertenencia crit
                ORDER BY c.id
            """, Object[].class).getResultList();

            Map<Integer, List<core.api.DTO.criterio.CriterioDTO>> criteriosPorColeccion = new HashMap<>();

            for (Object[] r : rowsCriterios) {
                Integer idColeccion = (Integer) r[0];
                Criterio crit = (Criterio) r[1];

                criteriosPorColeccion
                        .computeIfAbsent(idColeccion, k -> new ArrayList<>())
                        .add(core.api.DTO.criterio.CriterioDTO.from(crit));
            }

            // ARMAR DTOS
            List<ColeccionDTO> dtos = new ArrayList<>(bases.size());
            for (Object[] b : bases) {
                Integer id = (Integer) b[0];
                String titulo = (String) b[1];
                String descripcion = (String) b[2];
                String handle = (String) b[3];

                // tipos reales que vienen del JPQL
                ModoDeNavegacion modo = (ModoDeNavegacion) b[4];      // puede ser null
                AlgoritmoConsenso algoritmoObj = (AlgoritmoConsenso) b[5];     // puede ser null

                // pasar a String para el DTO
                String modoStr = (modo != null) ? modo.name() : null;

                String algoritmoStr = null;
                if (algoritmoObj != null) {
                    if (algoritmoObj instanceof StrategyAbsoluta) {
                        algoritmoStr = "ABSOLUTO";
                    } else if (algoritmoObj instanceof StrategyMayoriaSimple) {
                        algoritmoStr = "MAYORIA_SIMPLE";
                    } else if (algoritmoObj instanceof StrategyMultiplesMenciones) {
                        algoritmoStr = "MULTIPLES_MENCIONES";
                    } else {
                        algoritmoStr = algoritmoObj.getClass().getSimpleName();
                    }
                }

                ColeccionDTO dto = new ColeccionDTO();
                dto.setId(id);
                dto.setTitulo(titulo);
                dto.setDescripcionColeccion(descripcion);
                dto.setIdentificadorHandle(handle);
                dto.setModoDeNavegacion(modoStr);
                dto.setAlgoritmoConsenso(algoritmoStr);

                // cantidad de hechos totales
                dto.setCantidadHechos(
                        Math.toIntExact(conteos.getOrDefault(id, 0L))
                );

                // cantidad de hechos visibles
                dto.setCantidadHechosVisibles(
                        Math.toIntExact(conteosVisibles.getOrDefault(id, 0L))
                );

                // ids de las fuentes
                dto.setFuentes(
                        fuentesPorColeccion.getOrDefault(id, new ArrayList<>())
                );

                // criterios
                dto.setCriterioDePertenencia(
                        criteriosPorColeccion.getOrDefault(id, new ArrayList<>())
                );

                dtos.add(dto);
            }
            return dtos;

        } finally {
            try {
                em.close();
            } catch (Exception ignore) {
            }
        }
    }

    public ColeccionDTO obtenerColeccionDTOConCantidadHechos(Integer idColeccion) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            // =========================
            //  BASE: datos principales
            // =========================
            Object[] base = em.createQuery("""
            SELECT c.id,
                   c.titulo,
                   c.descripcionColeccion,
                   c.identificadorHandle,
                   c.modoDeNavegacion,
                   c.algoritmoConsenso
            FROM coleccion c
            WHERE c.id = :idColeccion
        """, Object[].class)
                    .setParameter("idColeccion", idColeccion)
                    .getSingleResult();

            Integer id = (Integer) base[0];
            String titulo = (String) base[1];
            String descripcion = (String) base[2];
            String handle = (String) base[3];
            ModoDeNavegacion modo = (ModoDeNavegacion) base[4];  // puede ser null
            AlgoritmoConsenso algoritmoObj = (AlgoritmoConsenso) base[5]; // puede ser null

            // =========================
            //  CANTIDAD DE HECHOS
            // =========================
            Long cantHechos = em.createQuery("""
            SELECT COUNT(h)
            FROM coleccion c
            LEFT JOIN c.hechos h
            WHERE c.id = :idColeccion
        """, Long.class)
                    .setParameter("idColeccion", idColeccion)
                    .getSingleResult();

            // =========================
            //  CANTIDAD DE HECHOS VISIBLES
            // =========================
            Long cantHechosVisibles = em.createQuery("""
            SELECT COUNT(hv)
            FROM coleccion c
            LEFT JOIN c.hechosVisibles hv
            WHERE c.id = :idColeccion
        """, Long.class)
                    .setParameter("idColeccion", idColeccion)
                    .getSingleResult();

            // =========================
            //  FUENTES (lista de IDs)
            // =========================
            List<Integer> fuentesIds = em.createQuery("""
            SELECT f.id
            FROM coleccion c
            JOIN c.fuentes f
            WHERE c.id = :idColeccion
            ORDER BY f.id
        """, Integer.class)
                    .setParameter("idColeccion", idColeccion)
                    .getResultList();

            // =========================
            //  CRITERIOS
            // =========================
            List<Object[]> rowsCriterios = em.createQuery("""
            SELECT c.id, crit
            FROM coleccion c
            JOIN c.criterioDePertenencia crit
            WHERE c.id = :idColeccion
            ORDER BY c.id
        """, Object[].class)
                    .setParameter("idColeccion", idColeccion)
                    .getResultList();

            List<core.api.DTO.criterio.CriterioDTO> criteriosDTO = new ArrayList<>();
            for (Object[] r : rowsCriterios) {
                Criterio crit = (Criterio) r[1];
                criteriosDTO.add(core.api.DTO.criterio.CriterioDTO.from(crit));
            }

            // =========================
            //  MAPEO A DTO
            // =========================
            String modoStr = (modo != null) ? modo.name() : null;

            String algoritmoStr = null;
            if (algoritmoObj != null) {
                if (algoritmoObj instanceof StrategyAbsoluta) {
                    algoritmoStr = "ABSOLUTO";
                } else if (algoritmoObj instanceof StrategyMayoriaSimple) {
                    algoritmoStr = "MAYORIA_SIMPLE";
                } else if (algoritmoObj instanceof StrategyMultiplesMenciones) {
                    algoritmoStr = "MULTIPLES_MENCIONES";
                } else {
                    algoritmoStr = algoritmoObj.getClass().getSimpleName();
                }
            }

            ColeccionDTO dto = new ColeccionDTO();
            dto.setId(id);
            dto.setTitulo(titulo);
            dto.setDescripcionColeccion(descripcion);
            dto.setIdentificadorHandle(handle);
            dto.setModoDeNavegacion(modoStr);
            dto.setAlgoritmoConsenso(algoritmoStr);

            dto.setCantidadHechos(Math.toIntExact(cantHechos != null ? cantHechos : 0L));
            dto.setCantidadHechosVisibles(Math.toIntExact(cantHechosVisibles != null ? cantHechosVisibles : 0L));

            dto.setFuentes(fuentesIds != null ? fuentesIds : new ArrayList<>());
            dto.setCriterioDePertenencia(criteriosDTO);

            return dto;

        } catch (NoResultException e) {
            // si querés, podés devolver null o tirar una excepción custom
            return null;
        } finally {
            try {
                em.close();
            } catch (Exception ignore) {
            }
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
            try {
                em.close();
            } catch (Exception ignore) {
            }
        }
    }

    public List<Hecho> obtenerHechosVisiblesDeColeccion(Integer idColeccion) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            Coleccion coleccion = em.find(Coleccion.class, idColeccion);
            if (coleccion == null) {
                return List.of();
            }

            // Fuerzo la inicialización dentro de la sesión
            List<Hecho> visibles = coleccion.getHechosVisibles();
            visibles.size(); // toca la colección para inicializarla

            // Devuelvo una lista "normal", desconectada de Hibernate
            return new ArrayList<>(visibles);
        } finally {
            try {
                em.close();
            } catch (Exception ignore) {
            }
        }
    }

    @Override
    public void delete(Coleccion entity) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            DBUtils.comenzarTransaccion(em);

            // 1. Re-attach la entidad si es necesario para poder acceder a sus colecciones
            Coleccion managed = em.contains(entity) ? entity : em.merge(entity);

            // 2. Guardamos los criterios asociados ANTES de borrar la colección
            // (Hacemos una copia de la lista para no tener problemas de concurrencia)
            List<Criterio> criteriosAsociados = new ArrayList<>(managed.getCriterioDePertenencia());

            // 3. Borramos la colección
            // Esto eliminará la colección y las filas de unión en 'coleccion_criterio'
            em.remove(managed);

            // Hacemos flush para que la BD actualice la tabla intermedia inmediatamente
            em.flush();

            // 4. Verificamos "huérfanos": ¿Quedó algún criterio suelto?
            for (Criterio c : criteriosAsociados) {
                // Contamos cuántas colecciones siguen usando este criterio específico
                Long count = em.createQuery(
                                "SELECT COUNT(c) FROM coleccion c JOIN c.criterioDePertenencia cr WHERE cr.id = :id",
                                Long.class)
                        .setParameter("id", c.getId())
                        .getSingleResult();

                // Si nadie más lo usa (count == 0), lo borramos
                if (count == 0) {
                    Criterio criterioABorrar = em.find(Criterio.class, c.getId());
                    if (criterioABorrar != null) {
                        em.remove(criterioABorrar);
                    }
                }
            }

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

    public void eliminarFuenteDeTodasLasColecciones(Integer idFuente) {
        if (idFuente == null) return;
        EntityManager em = DBUtils.getEntityManager();
        try {
            DBUtils.comenzarTransaccion(em);
            int filas = em.createNativeQuery(
                            "DELETE FROM coleccion_fuente WHERE id_fuente = ?1"
                    )
                    .setParameter(1, idFuente)
                    .executeUpdate();
            DBUtils.commit(em);
        } catch (RuntimeException ex) {
            DBUtils.rollback(em);
            throw ex;
        } finally {
            try { em.close(); } catch (Exception ignore) {}
        }
    }

    public Coleccion findByIdConCriteriosYFuentes(Integer id) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            return em.createQuery("""
            select distinct c from coleccion c
            left join fetch c.criterioDePertenencia
            left join fetch c.fuentes
            where c.id = :id
        """, Coleccion.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Solo criterios, sin fuentes
    public Coleccion findByIdConCriterios(Integer id) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            return em.createQuery("""
            select distinct c from coleccion c
            left join fetch c.criterioDePertenencia
            where c.id = :id
        """, Coleccion.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<Integer> obtenerIdsFuentesDeColeccion(Integer idColeccion) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            return em.createQuery("""
            select f.id from coleccion c
            join c.fuentes f
            where c.id = :id
        """, Integer.class)
                    .setParameter("id", idColeccion)
                    .getResultList();
        } finally {
            em.close();
        }
    }


}

