package core.models.repository;

import core.models.entities.colecciones.Coleccion;
import core.models.entities.solicitud.SolicitudDeEliminacion;
import utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

public class SolicitudEliminacionRepository extends JpaRepositoryBase<SolicitudDeEliminacion, Integer> {
    private static volatile SolicitudEliminacionRepository instance;

    private SolicitudEliminacionRepository() {
        super(SolicitudDeEliminacion.class, DBUtils::getEntityManager, SolicitudDeEliminacion::getId);
    }

    public static SolicitudEliminacionRepository getInstance() {
        if (instance == null) {
            synchronized (SolicitudEliminacionRepository.class) {
                if (instance == null) {
                    instance = new SolicitudEliminacionRepository();
                }
            }
        }
        return instance;
    }

}
