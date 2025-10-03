package core.models.entities.repository;

import core.models.entities.fuentes.TipoFuente;
import core.models.entities.hecho.Categoria;
import core.models.entities.hecho.Coordenadas;
import core.models.entities.hecho.Estado;
import core.models.entities.hecho.Hecho;
import core.models.repository.HechosRepository;
import core.models.repository.seeders.HechosRepositorySeeder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestHechosRepository {

    Coordenadas coordenadas1 = new Coordenadas(123.0, 456.0);

    Categoria categoriaIncendio = new Categoria("Incendio");


    Hecho hecho1 = new Hecho(coordenadas1, categoriaIncendio,
            null, null, null, Estado.ACEPTADO, null,
            LocalDate.now().minusDays(2), LocalDate.now().minusDays(3),
            TipoFuente.ESTATICA, null, "No hubo heridos, fue por una sartén", "incendio en Casa", null,null);
    HechosRepository hechosRepository = HechosRepository.getInstance();
    HechosRepositorySeeder hechosRepositorySeeder = HechosRepositorySeeder.getInstance();

    Hecho hecho2 = new Hecho( coordenadas1, categoriaIncendio,
            null, null, null, Estado.ACEPTADO, null,
            LocalDate.now().minusDays(2), LocalDate.now().minusDays(3),
            TipoFuente.ESTATICA, null, "No hubo heridos, fue por una sartén", "en casa", null,null);


    @BeforeEach
    void SetUp(){

    }

    @Test
    void agregarARepos(){
        hechosRepository.add(hecho1);
        hechosRepository.add(hecho2);
    }


    @Test
    void SIMatchDeTitulo(){
        hechosRepositorySeeder.cargarHechosSeeder();
        assertTrue(hechosRepository.esHechoDuplicado(hecho1));
    }


    @Test
    void NOMatchDeTitulo(){
        hechosRepositorySeeder.cargarHechosSeeder();

        assertFalse(hechosRepository.esHechoDuplicado(hecho2));
    }
}
