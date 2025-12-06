package core.models.repository;

import core.models.entities.colecciones.criterios.Criterio;
import utils.DBUtils;

public class CriteriosRepository extends JpaRepositoryBase<Criterio, Integer> {

    private static volatile CriteriosRepository instance;

    private CriteriosRepository() {
        super(Criterio.class, DBUtils::getEntityManager, Criterio::getId); // ajustá getId() si tu Criterio usa otro tipo
    }

    public static CriteriosRepository getInstance() {
        if (instance == null) {
            synchronized (CriteriosRepository.class) {
                if (instance == null) instance = new CriteriosRepository();
            }
        }
        return instance;
    }

    public Criterio getCriterio(Integer id) {
        return findById(id);
    }
}