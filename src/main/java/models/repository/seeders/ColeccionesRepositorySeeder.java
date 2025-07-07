package models.repository.seeders;

import models.entities.colecciones.Coleccion;
import models.entities.colecciones.CriterioDePertenencia;
import models.entities.fuentes.Fuente;
import models.entities.hecho.Hecho;
import models.repository.ColeccionesRepository;
import models.repository.FuentesRepository;
import models.repository.HechosRepository;

import java.util.ArrayList;
import java.util.List;

public class ColeccionesRepositorySeeder {
    private static volatile ColeccionesRepositorySeeder instance;

    public static ColeccionesRepositorySeeder getInstance() {
        if (instance == null) {
            synchronized (ColeccionesRepositorySeeder.class) {
                if (instance == null) {
                    instance = new ColeccionesRepositorySeeder();
                }
            }
        }
        return instance;
    }
    HechosRepository hechosRepository = HechosRepository.getInstance();

    //incendio, sin incidentes
    Hecho hecho1 = hechosRepository.getHecho(1);
    Hecho hecho3 = hechosRepository.getHecho(3);

    //choque, sin incidente
    Hecho hecho4 = hechosRepository.getHecho(4);
    Hecho hecho2 = hechosRepository.getHecho(2);

    //todos son sin victimas fatales
    Hecho hecho5 = hechosRepository.getHecho(5);

    List<Hecho> coleccionHechos1 = new ArrayList<>(List.of(hecho1, hecho3));
    List<Hecho> coleccionHechos2 = new ArrayList<>(List.of(hecho4, hecho2));
    List<Hecho> coleccionHechos3 = new ArrayList<>(List.of(hecho1, hecho3, hecho4, hecho2, hecho5));

    FuentesRepository fuentesRepository = FuentesRepository.getInstance();
    Fuente fuente1 = fuentesRepository.getFuente(1);
    Fuente fuente2 = fuentesRepository.getFuente(2);
    Fuente fuente3 = fuentesRepository.getFuente(3);

    List<Fuente> fuentes3 = new ArrayList<>(List.of(fuente1, fuente2, fuente3));
    List<Fuente> fuentes1 = List.of(fuente1);
    List<Fuente> fuentes2 = List.of(fuente2);

    ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();

    Coleccion coleccion1 = new Coleccion(1, "Incendios", "Incendios de cualquier objeto", (CriterioDePertenencia) null, fuentes1, coleccionHechos1, null);
    Coleccion coleccion2 = new Coleccion(2, "Choques", "Todos los choques", (CriterioDePertenencia) null, fuentes2, coleccionHechos2, null);
    Coleccion coleccion3 = new Coleccion(3, "Sin victimas fatales", "Accidentes de cualquier tipo sin accidentes", (CriterioDePertenencia) null, fuentes3, coleccionHechos3, null);

    public void cargarColeccionesRepositorySeeder()
    {
        if (fuente1 == null || fuente2 == null || fuente3 == null) {
            throw new IllegalStateException("Una o más fuentes no están cargadas en el repositorio");
        }
        coleccionesRepository.add(coleccion1);
        coleccionesRepository.add(coleccion2);
        coleccionesRepository.add(coleccion3);
    }

}
