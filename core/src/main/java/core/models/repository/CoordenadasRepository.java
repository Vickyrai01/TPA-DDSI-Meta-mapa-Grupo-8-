package core.models.repository;

import core.models.entities.hecho.Coordenadas;
import utils.DBUtils;

public class CoordenadasRepository extends JpaRepositoryBase<Coordenadas, Integer> {

    private static volatile CoordenadasRepository instance;

    private CoordenadasRepository() {
        super(Coordenadas.class, DBUtils::getEntityManager, Coordenadas::getId);
    }

    public static CoordenadasRepository getInstance() {
        if (instance == null) {
            synchronized (CoordenadasRepository.class) {
                if (instance == null) instance = new CoordenadasRepository();
            }
        }
        return instance;
    }

    public Coordenadas getCoordenadas(Integer idCoordenadas){
        return findById(idCoordenadas);
    }

}

