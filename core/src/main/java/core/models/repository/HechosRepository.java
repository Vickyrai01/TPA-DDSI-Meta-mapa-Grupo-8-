package core.models.repository;

import core.models.entities.fuentes.Fuente;
import core.models.entities.hecho.Categoria;
import core.models.entities.hecho.Coordenadas;
import core.models.entities.hecho.Hecho;
import core.models.entities.hecho.Contribuyente;
import utils.DBUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    public Hecho getHecho(int idHecho){
        return findById(idHecho);
    }

    public Hecho getHechoHash(String hash) {
        if (hash == null || hash.isBlank()) return null;

        EntityManager em = DBUtils.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT h FROM hecho h WHERE LOWER(h.hash) = LOWER(:hash)",
                            Hecho.class
                    )
                    .setParameter("hash", hash.trim())
                    .setMaxResults(1)          // por si hubiera más de uno
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public void addAllEnUnaTransaccion(List<Hecho> hechos) {
        if (hechos == null || hechos.isEmpty()) return;
        EntityManager em = DBUtils.getEntityManager();
        final int batch = 50;
        int i = 0;
        try {
            // (Opcional, para MySQL) bajar lock timeout en esta sesión:
            try { em.createNativeQuery("SET innodb_lock_wait_timeout = 5").executeUpdate(); } catch (Exception ignore) {}

            DBUtils.comenzarTransaccion(em);

            for (Hecho h : hechos) {
                // ---- BYPASS de colecciones que pueden disparar escrituras/joins ----
                // Si tus mapeos de etiquetas/sugerencias no están bien, pueden causar problemas.
                // Para aislar el issue: no persistamos esas colecciones en esta pasada.
                h.setSugerenciaDeCambio(null); // si es @OneToMany mal mapeado, evitá que Hibernate intente tocar esa tabla
                h.setEtiquetas(null);

                //Asegurar las clases
                if (h.getCategoria() == null || h.getUbicacion() == null)
                    throw new IllegalArgumentException("Hecho sin categoría o coordenadas");

                //Si la categoria existe, usarla
                if (h.getCategoria().getId() != null) {
                    h.setCategoria(em.getReference(Categoria.class, h.getCategoria().getId()));
                } else {
                    Categoria cat = null;

                    String nombreOriginal = h.getCategoria().getNombre();
                    String nombreLower = nombreOriginal.toLowerCase().trim();

                    // 1) Buscar la categoría tal cual viene (normalizada a lower)
                    try {
                        cat = em.createQuery("from categoria c where lower(c.nombre) = :n", Categoria.class)
                                .setParameter("n", nombreLower)
                                .setMaxResults(1)
                                .getSingleResult();
                    } catch (NoResultException e) {
                        // no encontrada, seguimos
                    }

                    // 2) Si no existe y termina en 's', probamos quitando la 's' (singular)
                    if (cat == null && nombreLower.endsWith("s")) {
                        String singularLower = nombreLower.substring(0, nombreLower.length() - 1);
                        try {
                            cat = em.createQuery("from categoria c where lower(c.nombre) = :n", Categoria.class)
                                    .setParameter("n", singularLower)
                                    .setMaxResults(1)
                                    .getSingleResult();
                        } catch (NoResultException e2) {
                            // tampoco existe en singular, seguimos
                        }
                    }

                    // 3) Si sigue sin existir, la creo "como vino"
                    if (cat == null) {
                        em.persist(h.getCategoria());
                        em.flush();
                        cat = h.getCategoria();
                    }

                    h.setCategoria(cat);
                }


                //Si la ubicacion existe, usarla
                if (h.getUbicacion().getId() != null) {
                    h.setUbicacion(em.getReference(Coordenadas.class, h.getUbicacion().getId()));
                } else {
                    Coordenadas coord;
                    try {
                        coord = em.createQuery(
                                        "from coordenadas c where c.latitud=:lat and c.longitud=:lon", Coordenadas.class)
                                .setParameter("lat", h.getUbicacion().getLatitud())
                                .setParameter("lon", h.getUbicacion().getLongitud())
                                .setMaxResults(1).getSingleResult();
                    } catch (NoResultException e) {
                        em.persist(h.getUbicacion());
                        em.flush();
                        coord = h.getUbicacion();
                    }
                    h.setUbicacion(coord);
                }

                // Contribuyente opcional: si viene, lo busco / creo; si no, lo dejo null
                if (h.getContribuyente() != null) {
                    core.models.entities.hecho.Contribuyente c = h.getContribuyente();

                    if (c.getId() != null) {
                        // Intento traer el existente
                        core.models.entities.hecho.Contribuyente existente =
                                em.find(core.models.entities.hecho.Contribuyente.class, c.getId());

                        if (existente != null) {
                            // Ya existe → uso ese
                            h.setContribuyente(existente);
                        } else {
                            // No existe en BD pero viene con id → lo trato como nuevo
                            c.setId(null); // opcional pero prolijo, así el persist no choca
                            em.persist(c);
                            em.flush();
                            h.setContribuyente(c);
                        }
                    } else {
                        // No tiene id → es nuevo, lo persisto
                        em.persist(c);
                        em.flush();
                        h.setContribuyente(c);
                    }
                } else {
                    h.setContribuyente(null);
                }


                // Evitar duplicados por hash/título dentro de la misma TX
                if (h.getHash() != null && !h.getHash().isBlank()) {
                    Long dup = em.createQuery("select count(x) from hecho x where lower(x.hash)=:hs", Long.class)
                            .setParameter("hs", h.getHash().toLowerCase()).getSingleResult();
                    if (dup > 0) continue; // saltar duplicado
                }

                em.persist(h);

                if (++i % batch == 0) { em.flush(); em.clear(); }
            }

            DBUtils.commit(em);
        } catch (RuntimeException ex) {
            DBUtils.rollback(em);
            throw ex;
        } finally {
            try { em.close(); } catch (Exception ignore) {}
        }
    }

    public int eliminarHechosPorIdFuente(Integer idFuente) {
        if (idFuente == null) return 0;

        EntityManager em = DBUtils.getEntityManager();
        try {
            DBUtils.comenzarTransaccion(em);

            // Traigo todos los hechos de esa fuente
            List<Hecho> hechos = em.createQuery(
                            "SELECT h FROM hecho h WHERE h.idFuente = :idFuente",
                            Hecho.class
                    )
                    .setParameter("idFuente", idFuente)
                    .getResultList();

            if (hechos.isEmpty()) {
                DBUtils.commit(em);
                return 0;
            }

            int eliminados = 0;

            for (Hecho hecho : hechos) {
                Integer id = hecho.getId();

                // Limpiar relaciones para evitar errores de FK
                try {
                    em.createNativeQuery("DELETE FROM coleccion_hecho WHERE id_hecho = :id")
                            .setParameter("id", id)
                            .executeUpdate();
                } catch (Exception ignore) {}

                try {
                    em.createNativeQuery("DELETE FROM hecho_etiqueta WHERE id_hecho = :id")
                            .setParameter("id", id)
                            .executeUpdate();
                } catch (Exception ignore) {}

                try {
                    em.createNativeQuery("DELETE FROM hecho_multimedia WHERE hecho_id = :id")
                            .setParameter("id", id)
                            .executeUpdate();
                } catch (Exception ignore) {}

                try {
                    em.createNativeQuery("DELETE FROM hechos_visibles WHERE id_hecho = :id")
                            .setParameter("id", id)
                            .executeUpdate();
                } catch (Exception ignore) {}

                try {
                    em.createNativeQuery("DELETE FROM solicitud_de_eliminacion WHERE hecho_id_hecho = :id")
                            .setParameter("id", id)
                            .executeUpdate();
                } catch (Exception ignore) {}

                // Eliminar el hecho
                Hecho managed = em.contains(hecho) ? hecho : em.merge(hecho);
                em.remove(managed);

                eliminados++;

                // Por si son muchos, flush/clear cada tanto
                if (eliminados % 50 == 0) {
                    em.flush();
                    em.clear();
                }
            }

            DBUtils.commit(em);
            return eliminados;

        } catch (RuntimeException ex) {
            DBUtils.rollback(em);
            throw ex;
        } finally {
            try { em.close(); } catch (Exception ignore) {}
        }
    }

}

