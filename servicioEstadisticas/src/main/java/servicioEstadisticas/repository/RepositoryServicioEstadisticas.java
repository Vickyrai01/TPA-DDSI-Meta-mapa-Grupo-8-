package servicioEstadisticas.repository;

import servicioEstadisticas.model.Hecho;
import servicioEstadisticas.model.Coordenadas;
import servicioEstadisticas.model.SolicitudSpam;
import utils.DBUtils;
import utils.GeocodingUtils;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
/*
    public static List<Map<String, Object>> provinciaConMasHechos() {
        EntityManager em = DBUtils.getEntityManager();
        try {
            String jpql = "SELECT h.provincia, COUNT(h) as cantidad " +
                    "FROM Hecho h " +
                    "GROUP BY h.provincia " +
                    "ORDER BY COUNT(h) DESC";

            List<Object[]> results = em.createQuery(jpql, Object[].class)
                    .getResultList();

            return results.stream()
                    .map(result -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("provincia", result[0]);
                        map.put("cantidad", result[1]);
                        return map;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }
*/
public static List<Map<String, Object>> provinciaConMasHechos() {
    EntityManager em = DBUtils.getEntityManager();
    try {
        String jpql = "SELECT c.latitud, c.longitud " +
                "FROM Hecho h " +
                "INNER JOIN h.coordenadas c";

        List<Object[]> coordenadas = em.createQuery(jpql, Object[].class)
                .getResultList();

        System.out.println("Número de coordenadas encontradas: " + coordenadas.size());

        Map<String, Long> provinciaCount = new HashMap<>();

        for (Object[] coord : coordenadas) {
            double lat = (Double) coord[0];
            double lon = (Double) coord[1];

            System.out.println("Procesando coordenadas: lat=" + lat + ", lon=" + lon);

            String provincia = utils.GeocodingUtils.obtenerProvincia(lat, lon);
            System.out.println("Provincia obtenida: " + provincia);

            if (provincia != null) {
                provinciaCount.merge(provincia, 1L, Long::sum);
            }
        }

        return provinciaCount.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("provincia", entry.getKey());
                    map.put("cantidad", entry.getValue());
                    return map;
                })
                .sorted((m1, m2) -> Long.compare(
                        (Long) m2.get("cantidad"),
                        (Long) m1.get("cantidad")
                ))
                .collect(Collectors.toList());
    } catch (Exception e) {
        e.printStackTrace();
        return Collections.emptyList();
    } finally {
        em.close();
    }
}


    public static List<Map<String, Object>> categoriaMasReportada() {
        EntityManager em = DBUtils.getEntityManager();
        try {
            String jpql = "SELECT h.categoria, COUNT(h) as cantidad " +
                    "FROM Hecho h " +
                    "GROUP BY h.categoria " +
                    "ORDER BY COUNT(h) DESC";

            List<Object[]> results = em.createQuery(jpql, Object[].class)
                    .getResultList();

            return results.stream()
                    .map(result -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("categoria", result[0]);
                        map.put("cantidad", result[1]);
                        return map;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    public static Map<String, Object> cantidadSolicitudesEliminacion() {
        EntityManager em = DBUtils.getEntityManager();
        try {
            // Consulta para obtener la cantidad de spam
            String jpqlSpam = "SELECT COUNT(s) FROM SolicitudSpam s WHERE s.fueSpam = true";
            Long cantidadSpam = em.createQuery(jpqlSpam, Long.class)
                    .getSingleResult();

            // Consulta para obtener el total de solicitudes
            String jpqlTotal = "SELECT COUNT(s) FROM SolicitudSpam s";
            Long cantidadTotal = em.createQuery(jpqlTotal, Long.class)
                    .getSingleResult();

            Map<String, Object> resultado = new HashMap<>();
            resultado.put("solicitudes spam", cantidadSpam);
            resultado.put("total de solicitudes", cantidadTotal);

            return resultado;
        } catch (Exception e) {
            Map<String, Object> errorResultado = new HashMap<>();
            errorResultado.put("solicitudes spam", 0);
            errorResultado.put("total de solicitudes", 0);
            return errorResultado;
        } finally {
            em.close();
        }
    }

    public static List<Map<String, Object>> horarioxCategoria(String categoria) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            String jpql = "SELECT FUNCTION('HOUR', h.fecha_suceso) as hora, COUNT(h) as cantidad " +
                    "FROM Hecho h " +
                    "WHERE h.categoria = :categoria " +
                    "GROUP BY FUNCTION('HOUR', h.fecha_suceso) " +
                    "ORDER BY COUNT(h) DESC";

            List<Object[]> resultados = em.createQuery(jpql, Object[].class)
                    .setParameter("categoria", categoria)
                    .getResultList();

            return resultados.stream()
                    .map(resultado -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("hora", resultado[0] + ":00");
                        map.put("cantidad", resultado[1]);
                        return map;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    public static List<Map<String, Object>> provinciaConMasHechosEnCategoria(String categoria) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            String jpql = "SELECT h.provincia, COUNT(h) as cantidad " +
                    "FROM Hecho h " +
                    "WHERE h.categoria = :categoria " +
                    "GROUP BY h.provincia " +
                    "ORDER BY COUNT(h) DESC";

            List<Object[]> resultados = em.createQuery(jpql, Object[].class)
                    .setParameter("categoria", categoria)
                    .getResultList();

            return resultados.stream()
                    .map(resultado -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("provincia", resultado[0]);
                        map.put("cantidad", resultado[1]);
                        return map;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
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