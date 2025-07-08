package models.repository.seeders;

import models.entities.fuentes.TipoFuente;
import models.entities.hecho.Coordenadas;
import models.entities.hecho.Estado;
import models.entities.hecho.Hecho;
import models.repository.HechosRepository;

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


    Hecho hecho1 = new Hecho(1, coordenadas1, "incendio", null,
            null, null, Estado.ACEPTADO, null,
            LocalDate.now().minusDays(2), LocalDate.now().minusDays(3),
            TipoFuente.ESTATICA, null, "No hubo heridos, fue por una sartén", "Incendio en casa");
    Hecho hecho2 = new Hecho(2, coordenadas2, "choque", null,
            null, null, Estado.ACEPTADO, null,
            LocalDate.now().minusDays(2), LocalDate.now().minusDays(3),
            TipoFuente.ESTATICA, null, "Un perro cruzo por la calle y frenó de golpe, todos a salvo.", "Choque entre moto y gol");
    Hecho hecho3 = new Hecho(3, coordenadas3, "incendio", null,
            null, null, Estado.ACEPTADO, null,
            LocalDate.now().minusDays(1), LocalDate.now().minusDays(2),
            TipoFuente.ESTATICA, null, "Causa desconocida", "Departamento en un edicio");
    Hecho hecho4 = new Hecho(4, coordenadas4, "choque", null,
            null, null, Estado.ACEPTADO, null,
            LocalDate.now().minusDays(1), LocalDate.now().minusDays(2),
            TipoFuente.ESTATICA, null, "Parecía que el conductor iba borracho, se llevó puesto una maceta que estaba en la calle", "Choque con maceta");
    Hecho hecho5 = new Hecho(5, coordenadas1, "robo", null,
            null, null, Estado.ACEPTADO, null,
            LocalDate.now().minusDays(5), LocalDate.now().minusDays(6),
            TipoFuente.ESTATICA, null, "Se robó unas manzanas y bolsas", "Hurto en una verdulería");

    HechosRepository hechosRepository = HechosRepository.getInstance();

    public void cargarHechosSeeder() {
        hechosRepository.add(hecho1);
        hechosRepository.add(hecho2);
        hechosRepository.add(hecho3);
        hechosRepository.add(hecho4);
        hechosRepository.add(hecho5);
    }
}