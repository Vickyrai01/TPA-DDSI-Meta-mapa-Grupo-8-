package core.models.repository.seeders;

import core.models.entities.colecciones.Coleccion;
import core.models.entities.colecciones.TipoConsenso;
import core.models.entities.colecciones.criterios.Criterio;
import core.models.entities.colecciones.criterios.CriterioDescripcion;
import core.models.entities.fuentes.Fuente;
import core.models.entities.hecho.Hecho;
import core.models.repository.ColeccionesRepository;
import core.models.repository.FuentesRepository;
import core.models.repository.HechosRepository;

import java.util.ArrayList;
import java.util.List;
public class ColeccionesRepositorySeeder {
    private static volatile ColeccionesRepositorySeeder instance;
    private final ColeccionesRepository coleccionesRepository;
    private final HechosRepository hechosRepository;
    private final FuentesRepository fuentesRepository;

    private ColeccionesRepositorySeeder() {
        this.coleccionesRepository = ColeccionesRepository.getInstance();
        this.hechosRepository = HechosRepository.getInstance();
        this.fuentesRepository = FuentesRepository.getInstance();
    }

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

    public void cargarColeccionesRepositorySeeder() {
        // Obtener hechos (validar que existan)
        Hecho hecho1 = hechosRepository.findById(1);
        Hecho hecho2 = hechosRepository.findById(2);
        Hecho hecho3 = hechosRepository.findById(3);
        Hecho hecho4 = hechosRepository.findById(4);
        Hecho hecho5 = hechosRepository.findById(5);

        if (hecho1 == null || hecho2 == null || hecho3 == null || hecho4 == null || hecho5 == null) {
            throw new IllegalStateException("No se encontraron todos los hechos necesarios");
        }

        // Obtener fuentes (validar que existan)
        Fuente fuente1 = fuentesRepository.findById(1);
        Fuente fuente2 = fuentesRepository.findById(2);
        Fuente fuente3 = fuentesRepository.findById(3);

        if (fuente1 == null || fuente2 == null || fuente3 == null) {
            throw new IllegalStateException("No se encontraron todas las fuentes necesarias");
        }

        // Crear listas
        List<Hecho> coleccionHechos1 = List.of(hecho1, hecho3);
        List<Hecho> coleccionHechos2 = List.of(hecho4, hecho2);
        List<Hecho> coleccionHechos3 = List.of(hecho1, hecho3, hecho4, hecho2, hecho5);

        List<Fuente> fuentes1 = List.of(fuente1);
        List<Fuente> fuentes2 = List.of(fuente2);
        List<Fuente> fuentes3 = List.of(fuente1, fuente2, fuente3);

        // Crear colecciones
        List<Criterio> criterios = new ArrayList<>();
        CriterioDescripcion criterioDescripcion =  new CriterioDescripcion("perro");
        criterios.add(criterioDescripcion);
        Coleccion coleccion1 = new Coleccion(1, "Incendios", "Incendios de cualquier objeto", criterios, fuentes1, coleccionHechos1, null);
        coleccion1.cambiarAlgoritmoConsenso(TipoConsenso.ABSOLUTO);
        Coleccion coleccion2 = new Coleccion(2, "Choques", "Todos los choques", criterios, fuentes2, coleccionHechos2, null);
        Coleccion coleccion3 = new Coleccion(3, "Sin victimas fatales", "Accidentes de cualquier tipo sin accidentes", criterios, fuentes3, coleccionHechos3, null);

        // Guardar colecciones
        coleccionesRepository.add(coleccion1);
        coleccionesRepository.add(coleccion2);
        coleccionesRepository.add(coleccion3);
    }
}