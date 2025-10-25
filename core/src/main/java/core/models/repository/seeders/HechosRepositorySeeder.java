package core.models.repository.seeders;

import core.models.agregador.normalizador.NormalizadorCategoria;
import core.models.entities.colecciones.criterios.CriterioNombre;
import core.models.entities.fuentes.TipoFuente;
import core.models.entities.hecho.Categoria;
import core.models.entities.hecho.Coordenadas;
import core.models.entities.hecho.Estado;
import core.models.entities.hecho.Hecho;
import core.models.repository.CategoriaRepository;
import core.models.repository.HechosRepository;

import java.time.LocalDate;

public class HechosRepositorySeeder {

    private static volatile HechosRepositorySeeder instance;

    public static HechosRepositorySeeder getInstance() {
        if (instance == null) {
            synchronized (HechosRepositorySeeder.class) {
                if (instance == null) {
                    instance = new HechosRepositorySeeder();
                }
            }
        }
        return instance;
    }

    Coordenadas coordenadas1 = new Coordenadas(123.0, 456.0);
    Coordenadas coordenadas2 = new Coordenadas(893.0, 016.0);
    Coordenadas coordenadas3 = new Coordenadas(973.0, 656.0);
    Coordenadas coordenadas4 = new Coordenadas(223.0, 033.0);

    HechosRepository hechosRepository = HechosRepository.getInstance();
    CategoriaRepository categoriaRepository = CategoriaRepository.getInstance();


    public void cargarHechosSeeder() {

        Categoria categoriaIncendio = new Categoria("Incendio");
        Categoria categoriaChoque = new Categoria("Choque");
        Categoria categoriaRobo = new Categoria("Robo");

        categoriaRepository.add(categoriaIncendio);
        categoriaRepository.add(categoriaChoque);
        categoriaRepository.add(categoriaRobo);
        Hecho hecho1 = new Hecho(null, categoriaIncendio, null,
                null, null, Estado.ACEPTADO, null,
                LocalDate.now().minusDays(2), LocalDate.now().minusDays(3),
                TipoFuente.ESTATICA, null, "No hubo heridos, fue por una sartén", "Incendio en casa", "C1",1);
        Hecho hecho2 = new Hecho(null, categoriaChoque, null,
                null, null, Estado.ACEPTADO, null,
                LocalDate.now().minusDays(2), LocalDate.now().minusDays(3),
                TipoFuente.ESTATICA, null, "Un perro cruzo por la calle y frenó de golpe, todos a salvo.", "Choque entre moto y gol", "C1",1);
        Hecho hecho3 = new Hecho(null, categoriaIncendio, null,
                null, null, Estado.ACEPTADO, null,
                LocalDate.now().minusDays(1), LocalDate.now().minusDays(2),
                TipoFuente.ESTATICA, null, "Causa desconocida", "Departamento en un edicio", "C3",1);
        Hecho hecho4 = new Hecho(null, categoriaChoque, null,
                null, null, Estado.ACEPTADO, null,
                LocalDate.now().minusDays(1), LocalDate.now().minusDays(2),
                TipoFuente.ESTATICA, null, "Parecía que el conductor iba borracho, se llevó puesto una maceta que estaba en la calle", "Choque con maceta", "C2",1);
        Hecho hecho5 = new Hecho( null, categoriaRobo, null,
                null, null, Estado.ACEPTADO, null,
                LocalDate.now().minusDays(5), LocalDate.now().minusDays(6),
                TipoFuente.ESTATICA, null, "Se robó unas manzanas y bolsas", "Hurto en una verdulería", "C4",1);
        hechosRepository.add(hecho1);
        hechosRepository.add(hecho2);
        hechosRepository.add(hecho3);
        hechosRepository.add(hecho4);
        hechosRepository.add(hecho5);
    }
}