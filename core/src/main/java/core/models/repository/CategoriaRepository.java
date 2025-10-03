package core.models.repository;

import core.models.entities.colecciones.Coleccion;
import core.models.entities.hecho.Categoria;
import utils.DBUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRepository extends JpaRepositoryBase<Categoria, Integer>{

        private static volatile CategoriaRepository instance;

        private CategoriaRepository() {
        super(Categoria.class, DBUtils::getEntityManager, Categoria::getId);
         }

        public static CategoriaRepository getInstance() {
            if (instance == null) {
                synchronized (CategoriaRepository.class) {
                    if (instance == null) {
                        instance = new CategoriaRepository();
                    }
                }
            }
            return instance;
        }


    public Categoria buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) return null;

        EntityManager em = DBUtils.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM categoria c WHERE LOWER(TRIM(c.nombre)) = LOWER(:nombre)",
                            Categoria.class)
                    .setParameter("nombre", nombre.trim())
                    .getResultStream() // evita excepción si no hay resultado
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }

    public boolean existe(String nombre) {
        return buscarPorNombre(nombre) != null;
    }



    }


