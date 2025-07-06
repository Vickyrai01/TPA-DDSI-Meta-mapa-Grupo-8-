package models.repository.seeders;

import models.entities.colecciones.Coleccion;
import models.entities.colecciones.CriterioDePertenencia;
import models.entities.hecho.Hecho;
import models.repository.ColeccionesRepository;
import models.repository.HechosRepository;

import java.util.ArrayList;
import java.util.List;

public class ColeccionesRepositorySeeder {
    private static volatile ColeccionesRepositorySeeder instance;

    public static ColeccionesRepositorySeeder getInstance() {
        if (instance == null) {
            synchronized (ColeccionesRepository.class) {
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

    ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();

    Coleccion coleccion1 = new Coleccion(1, "Incendios", "Incendios de cualquier objeto", (CriterioDePertenencia) null, null, coleccionHechos1, null);
    Coleccion coleccion2 = new Coleccion(2, "Choques", "Todos los choques", (CriterioDePertenencia) null, null, coleccionHechos2, null);
    Coleccion coleccion3 = new Coleccion(3, "Sin victimas fatales", "Accidentes de cualquier tipo sin accidentes", (CriterioDePertenencia) null, null, coleccionHechos3, null);

    public void cargarColeccionesRepositorySeeder()
    {  coleccionesRepository.add(coleccion1);
        coleccionesRepository.add(coleccion2);
        coleccionesRepository.add(coleccion3);
    }

}
