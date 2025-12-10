package core.models.repository;

import core.models.entities.hecho.Contribuyente;
import utils.DBUtils;

public class ContribuyentesRepository extends JpaRepositoryBase<Contribuyente, Integer> {

    private static volatile ContribuyentesRepository instance;

    private ContribuyentesRepository() {
        super(Contribuyente.class, DBUtils::getEntityManager, Contribuyente::getId);
    }

    public static ContribuyentesRepository getInstance() {
        if (instance == null) {
            synchronized (ContribuyentesRepository.class) {
                if (instance == null) instance = new ContribuyentesRepository();
            }
        }
        return instance;
    }

    public Contribuyente getContribuyente(Integer idContribuyente){
        return findById(idContribuyente);
    }

    
}
