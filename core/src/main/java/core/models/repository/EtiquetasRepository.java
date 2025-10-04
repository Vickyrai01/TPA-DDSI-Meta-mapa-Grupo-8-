package core.models.repository;

import core.models.entities.hecho.Etiqueta;
import utils.DBUtils;

public class EtiquetasRepository extends JpaRepositoryBase<Etiqueta, Integer> {

    private static volatile EtiquetasRepository instance;

    private EtiquetasRepository() {
        super(Etiqueta.class, DBUtils::getEntityManager, Etiqueta::getId);
    }

    public static EtiquetasRepository getInstance() {
        if (instance == null) {
            synchronized (EtiquetasRepository.class) {
                if (instance == null) instance = new EtiquetasRepository();
            }
        }
        return instance;
    }

    public Etiqueta getEtiqueta(Integer idEtiqueta){
        return findById(idEtiqueta);
    }

}