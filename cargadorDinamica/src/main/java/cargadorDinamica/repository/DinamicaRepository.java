package cargadorDinamica.repository;

import cargadorDinamica.model.HechoAIntegrarDTO;
import utils.DBUtils;

import javax.persistence.EntityManager;
import java.util.*;

public class DinamicaRepository extends JpaRepositoryBase<HechoAIntegrarDTO, String> {
    private static volatile DinamicaRepository instance;

    private DinamicaRepository() {
        super(HechoAIntegrarDTO.class, DBUtils::getEntityManager, HechoAIntegrarDTO::getHash);
    }

    private List<Map<HechoAIntegrarDTO, Boolean>> tablaHechos =
            new ArrayList<>(Arrays.asList(new HashMap<>(), new HashMap<>()));


    public static DinamicaRepository getInstance() {
        if (instance == null) {
            synchronized (DinamicaRepository.class) {
                if (instance == null) {
                    instance = new DinamicaRepository();
                }
            }
        }
        return instance;
    }

    public List<HechoAIntegrarDTO> getHechosNoProcesados() {
        List<HechoAIntegrarDTO> noProcesados = new ArrayList<>();

        EntityManager em = DBUtils.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT h FROM HechoAIntegrarDTO h WHERE h.fueExtraido = false"
                            ,HechoAIntegrarDTO.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
