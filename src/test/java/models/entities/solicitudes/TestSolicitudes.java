package models.entities.solicitudes;

import models.entities.fuentes.TipoFuente;
import models.entities.hecho.Categoria;
import models.entities.hecho.Coordenadas;
import models.entities.hecho.Estado;
import models.entities.hecho.Hecho;
import models.entities.solicitud.SolicitudDeEliminacion;
import models.repository.HechosRepository;
import models.repository.seeders.HechosRepositorySeeder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestSolicitudes {

    Coordenadas coordenadas1 = new Coordenadas(123.0, 456.0);

    Categoria categoriaIncendio = new Categoria("Incendio");


    Hecho hecho1 = new Hecho(1, coordenadas1, categoriaIncendio,
            null, null, null, Estado.ACEPTADO, null,
            LocalDate.now().minusDays(2), LocalDate.now().minusDays(3),
            TipoFuente.ESTATICA, null, "No hubo heridos, fue por una sartén", "incendio en Casa", null);
    HechosRepository hechosRepository = HechosRepository.getInstance();
    HechosRepositorySeeder hechosRepositorySeeder = HechosRepositorySeeder.getInstance();

    SolicitudDeEliminacion soli1 = new SolicitudDeEliminacion(1, hecho1, "aaawdkjas", null, null);

    @Test
    void seRechaza(){
        soli1.revisarPorSpam();

        assertFalse(soli1.getAceptada());
    }


    @Test
    void seAcepta(){
        soli1.aceptarSolicitud();

        assertTrue(soli1.getAceptada());
    }
}
