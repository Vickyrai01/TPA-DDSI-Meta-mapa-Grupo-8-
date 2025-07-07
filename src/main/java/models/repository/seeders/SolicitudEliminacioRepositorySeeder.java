package models.repository.seeders;


import models.entities.hecho.Hecho;
import models.entities.solicitud.SolicitudDeEliminacion;
import models.repository.HechosRepository;
import models.repository.SolicitudEliminacionRepository;

public class SolicitudEliminacioRepositorySeeder {

    private static volatile SolicitudEliminacioRepositorySeeder instance;

    public static SolicitudEliminacioRepositorySeeder getInstance() {
        if (instance == null) {
            synchronized (HechosRepository.class) {
                if (instance == null) {
                    instance = new SolicitudEliminacioRepositorySeeder();
                }
            }
        }
        return instance;
    }

    HechosRepository hechosRepository = HechosRepository.getInstance();

    Hecho hecho1 = hechosRepository.getHecho(1);
    Hecho hecho2 = hechosRepository.getHecho(2);
    Hecho hecho4 = hechosRepository.getHecho(4);

    SolicitudEliminacionRepository solicitudDeEliminacion = SolicitudEliminacionRepository.getInstance();

    SolicitudDeEliminacion solicitudDeEliminacion1 = new SolicitudDeEliminacion(1, hecho1,"Cualquier cosa dice, es mi casa. No hubo ningun incendio", null, null);
    SolicitudDeEliminacion solicitudDeEliminacion2 = new SolicitudDeEliminacion(2, hecho2,"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", null, null);
    SolicitudDeEliminacion solicitudDeEliminacion3 = new SolicitudDeEliminacion(3, hecho4,"espero que la persona este bien", null, null);

    public void cargarSolicitudDeEliminacionSeeder()
    {solicitudDeEliminacion.add(solicitudDeEliminacion1);
        solicitudDeEliminacion.add(solicitudDeEliminacion2);
        solicitudDeEliminacion.add(solicitudDeEliminacion3);
    }
}
