package core.models.entities.colecciones;

import core.models.entities.fuentes.Fuente;
import core.models.entities.hecho.Hecho;
import core.models.repository.ColeccionesRepository;
import utils.DBUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class SchedulerActualizarVisibles {
    private final ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();

    public void ejecutarSiHayBajaCarga(){
        if (hayBajaCargaEnSistema()){
            ejecutar();
        }
    }

    public boolean hayBajaCargaEnSistema(){ //suposición
        int hora = java.time.LocalTime.now().getHour();
        //return hora >= 1 && hora <= 6;
        return true; //PARA PROBAR
    }

    public void ejecutar() {
            System.out.println("Iniciando actualización de visibles...");
            EntityManager em = DBUtils.getEntityManager();
            List<Integer> idsColecciones;

            try {
                // 1. Obtenemos solo los IDs para no sobrecargar la memoria
                idsColecciones = em.createQuery("SELECT c.id FROM coleccion c", Integer.class).getResultList();
            } finally {
                em.close();
            }

            // 2. Procesamos cada colección en su propia transacción
            int procesadas = 0;
            for (Integer id : idsColecciones) {
                procesarColeccion(id);
                procesadas++;
            }

            System.out.println("Finalizado. Colecciones actualizadas: " + procesadas);
    }

    private void procesarColeccion(Integer id) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            DBUtils.comenzarTransaccion(em);

            // 3. Cargamos la colección Y sus hechos en una sola consulta (JOIN FETCH)
            // Esto evita la LazyInitializationException porque 'hechos' ya viene cargado.
            Coleccion coleccion = em.createQuery(
                            "SELECT DISTINCT c FROM coleccion c LEFT JOIN FETCH c.hechos WHERE c.id = :id",
                            Coleccion.class)
                    .setParameter("id", id)
                    .getSingleResult();

            // 4. Ejecutamos la lógica (ahora 'hechos' está disponible)
            coleccion.actualizarColeccionVisible();

            // 5. Guardamos los cambios (Hibernate detecta el cambio en 'hechosVisibles')
            em.merge(coleccion);

            DBUtils.commit(em);
        } catch (Exception e) {
            DBUtils.rollback(em);
            System.err.println("Error procesando colección ID " + id + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
