package core.models.repository;

import core.models.agregador.HechoAIntegrarDTO;
import core.models.entities.fuentes.Fuente;
import utils.DBUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class RevisionManualRepository extends JpaRepositoryBase<HechoAIntegrarDTO, String> {

    private static volatile RevisionManualRepository instance;

    private RevisionManualRepository() {
        super(HechoAIntegrarDTO.class, DBUtils::getEntityManager, HechoAIntegrarDTO::getHash);
    }

    public static RevisionManualRepository getInstance() {
        if (instance == null) {
            synchronized (RevisionManualRepository.class) {
                if (instance == null) {
                    instance = new RevisionManualRepository();
                }
            }
        }
        return instance;
    }

    private final List<HechoAIntegrarDTO> hechos = new ArrayList<>();

    //HACER
    public Boolean existeElHecho(String hash){
        EntityManager em = DBUtils.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END FROM hecho h WHERE h.hash = :hash",
                            Boolean.class)
                    .setParameter("hash", hash)
                    .getSingleResult();


        } finally {
            em.close();
        }
    }

}
