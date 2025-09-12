package models.entities.repository;

import models.entities.fuentes.TipoFuente;
import models.entities.hecho.Categoria;
import models.entities.hecho.Coordenadas;
import models.entities.hecho.Estado;
import models.entities.hecho.Hecho;
import models.repository.HechosRepository;
import models.repository.seeders.HechosRepositorySeeder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestHechosRepository {

    Coordenadas coordenadas1 = new Coordenadas(123.0, 456.0);

    Categoria categoriaIncendio = new Categoria("Incendio");


    Hecho hecho1 = new Hecho(1, coordenadas1, categoriaIncendio,
            null, null, null, Estado.ACEPTADO, null,
            LocalDate.now().minusDays(2), LocalDate.now().minusDays(3),
            TipoFuente.ESTATICA, null, "No hubo heridos, fue por una sartén", "incendio en Casa", null,"12");
    HechosRepository hechosRepository = HechosRepository.getInstance();
    HechosRepositorySeeder hechosRepositorySeeder = HechosRepositorySeeder.getInstance();

    Hecho hecho2 = new Hecho(1, coordenadas1, categoriaIncendio,
            null, null, null, Estado.ACEPTADO, null,
            LocalDate.now().minusDays(2), LocalDate.now().minusDays(3),
            TipoFuente.ESTATICA, null, "No hubo heridos, fue por una sartén", "en casa", null,"12");

    @BeforeEach
    void SetUp(){

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
