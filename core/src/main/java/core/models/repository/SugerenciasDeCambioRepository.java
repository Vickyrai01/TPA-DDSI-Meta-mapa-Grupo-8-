package core.models.repository;

import core.models.entities.hecho.SugerenciaDeCambio;
import utils.DBUtils;

public class SugerenciasDeCambioRepository extends JpaRepositoryBase<SugerenciaDeCambio, Integer> {

    private static volatile SugerenciasDeCambioRepository instance;

    private SugerenciasDeCambioRepository() {
        super(SugerenciaDeCambio.class, DBUtils::getEntityManager, SugerenciaDeCambio::getId);
    }

    public static SugerenciasDeCambioRepository getInstance() {
        if (instance == null) {
            synchronized (SugerenciasDeCambioRepository.class) {
                if (instance == null) instance = new SugerenciasDeCambioRepository();
            }
        }
        return instance;
    }

    public SugerenciaDeCambio getSugerenciaDeCambio(Integer idSugerenciaDeCambio){
        return findById(idSugerenciaDeCambio);
    }

}
