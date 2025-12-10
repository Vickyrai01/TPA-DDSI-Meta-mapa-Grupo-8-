package core.models.repository;

import core.models.entities.usuario.Usuario;
import org.springframework.stereotype.Repository;
import utils.DBUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;

@Repository
public class UsuarioRepository extends JpaRepositoryBase<Usuario, Integer> {

    private static volatile UsuarioRepository instance;

    private UsuarioRepository() {
        super(Usuario.class, DBUtils::getEntityManager, Usuario::getId);
    }

    public static UsuarioRepository getInstance() {
        if (instance == null) {
            synchronized (UsuarioRepository.class) {
                if (instance == null) instance = new UsuarioRepository();
            }
        }
        return instance;
    }

    public Optional<Usuario> findByCorreo(String correo) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            if (correo == null) return Optional.empty();

            String normalizado = correo.trim().toLowerCase();
            System.out.println("[CORE] Buscando usuario por correo normalizado: '" + normalizado + "'");

            Usuario usuario = em.createQuery(
                            "SELECT u FROM Usuario u WHERE LOWER(u.correo) = :correo", Usuario.class)
                    .setParameter("correo", normalizado)
                    .getSingleResult();

            return Optional.of(usuario);
        } catch (NoResultException e) {
            System.out.println("[CORE] No se encontró usuario para correo: '" + correo + "'");
            return Optional.empty();
        } finally {
            em.close();
        }
    }

}